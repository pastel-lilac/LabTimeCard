package com.batch.labtimecard.log

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.batch.labtimecard.common.dateString
import com.batch.labtimecard.common.dp
import com.batch.labtimecard.common.setTextColorRes
import com.batch.labtimecard.log.databinding.ActivityLogBinding
import com.kizitonwose.calendarview.CalendarView.Companion.DAY_SIZE_SQUARE
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import kotlinx.android.synthetic.main.item_calendar_day.view.*
import kotlinx.android.synthetic.main.item_calendar_header.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.WeekFields
import java.util.*

class LogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogBinding

    private val today = LocalDate.now()

    private val viewModel: LogViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_log)
        observeLogs()
        val memberKey = intent.getStringExtra(MEMBER)
        viewModel.memberKey = memberKey
        setUpCalendar()
    }

    private fun observeLogs() {
        viewModel.logs.observe(this, Observer {
            it.forEach {log ->
                val key = LocalDate.parse(log.date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                val logTime = log.time ?: return@forEach
                viewModel.events[key] = logTime
            }
        })
    }

    private fun setUpCalendar() {
        val daysOfWeek = daysOfWeekFromLocale()
        val currentMonth = YearMonth.now()
        binding.calendar.setup(
            currentMonth.minusMonths(10),
            currentMonth.plusMonths(10),
            daysOfWeek.first()
        )
        binding.calendar.dayHeight = 120.dp
        binding.calendar.dayWidth = 120.dp
        binding.calendar.scrollToMonth(currentMonth)

        selectDate(today)

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay // Will be set when this container is bound.
            val textView = view.day_text
            val dotView = view.day_dot

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        selectDate(day.date)
                    }
                }
            }
        }
        binding.calendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                val dotView = container.dotView

                textView.text = day.date.dayOfMonth.toString()

                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.isVisible = true
                    when (day.date) {
                        today -> {
                            textView.setTextColorRes(android.R.color.black)
                            textView.setBackgroundResource(R.drawable.shape_calendar_today)
                            dotView.isVisible = false
                        }
                        viewModel.selectedDate -> {
                            textView.setTextColorRes(R.color.common_inmr_lab)
                            textView.setBackgroundResource(R.drawable.shape_calendar_selected)
                            dotView.isVisible = false
                        }
                        else -> {
                            textView.setTextColorRes(android.R.color.black)
                            textView.background = null
                            dotView.isVisible = viewModel.events.contains(day.date)
                        }
                    }
                } else {
                    textView.isVisible = false
                    dotView.isVisible = false
                }
            }
        }
        binding.calendar.monthScrollListener = {
            binding.month.text = if (it.year == today.year) {
                DateTimeFormatter.ofPattern("M月").format(it.yearMonth)
            } else {
                DateTimeFormatter.ofPattern("yyyy年M月").format(it.yearMonth)
            }
            val date = it.yearMonth.atDay(1).atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .let { d -> DateTimeUtils.toDate(d) }
            viewModel.fetchLogByMonth(date)
            selectDate(it.yearMonth.atDay(1))
        }
        class MonthViewContainer(view: View) : ViewContainer(view) {
            val legendLayout = view.legendLayout
        }
        binding.calendar.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                // Setup each header day text if we have not done that already.
                if (container.legendLayout.tag == null) {
                    container.legendLayout.tag = month.yearMonth
                    container.legendLayout.children.map { it as TextView }
                        .forEachIndexed { index, tv ->
                            tv.text = daysOfWeekFromLocale()[index].name.first().toString()
                            tv.setTextColorRes(android.R.color.black)
                        }
                }
            }
        }
    }

    private fun selectDate(date: LocalDate) {
        if (viewModel.selectedDate != date) {
            val oldDate = viewModel.selectedDate
            viewModel.selectedDate = date
            oldDate?.let { binding.calendar.notifyDateChanged(it) }
            binding.calendar.notifyDateChanged(date)
            val dateString = date.atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .let { DateTimeUtils.toDate(it) }
                .dateString
            val log = viewModel.logs.value?.find { it.date == dateString }
            log?.let {
                binding.date.text = it.date
                binding.textWork.text = it.time?.loginTime
                binding.textOff.text = it.time?.logoutTime
            }
        }
    }

    private fun daysOfWeekFromLocale(): Array<DayOfWeek> {
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        var daysOfWeek = DayOfWeek.values()
        // Order `daysOfWeek` array so that firstDayOfWeek is at index 0.
        if (firstDayOfWeek != DayOfWeek.MONDAY) {
            val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
            val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
            daysOfWeek = rhs + lhs
        }
        return daysOfWeek
    }

    companion object {
        private const val MEMBER = "memberKey"

        fun createIntent(activity: Activity, member: String?) =
            Intent(activity, LogActivity::class.java).apply {
                putExtra(MEMBER, member)
            }

    }
}
