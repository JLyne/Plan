import React from "react";
import FullCalendar from '@fullcalendar/react'
import dayGridPlugin from '@fullcalendar/daygrid'
import interactionPlugin from '@fullcalendar/interaction'

const ServerCalendar = ({series, firstDay, onSelect}) => {
    return (
        <div id={'server-calendar'}>
            <FullCalendar
                plugins={[interactionPlugin, dayGridPlugin]}
                timeZone="UTC"
                themeSystem='bootstrap'
                eventColor='#2196F3'
                // dayMaxEventRows={4}
                firstDay={firstDay}
                initialView='dayGridMonth'
                navLinks={true}
                height={800}
                contentHeight={800}
                headerToolbar={{
                    left: 'title',
                    center: '',
                    right: 'dayGridMonth dayGridWeek dayGridDay today prev next'
                }}
                editable={Boolean(onSelect)}
                selectable={Boolean(onSelect)}
                select={onSelect}
                unselectAuto={true}
                events={(_fetchInfo, successCallback) => successCallback(series)}
            />
        </div>
    )
}

export default ServerCalendar