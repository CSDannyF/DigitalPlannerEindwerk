
//Dark Mode Toggle
document.querySelector('.dark-mode-switch').onclick = () => {
    document.querySelector('body').classList.toggle('dark');
    document.querySelector('body').classList.toggle('light');
};

//Check leap Year
isCheckYear = (year) => {
    return (year % 4 === 0 && year % 100 !== 0 && year % 400 !== 0)
        || (year % 100 === 0 && year % 400 === 0)
};

getFebDays = (year) => {
    return isCheckYear(year) ? 29 : 28
};

let calendar = document.querySelector('.calendar');
const monthNames = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
let monthPicker = document.querySelector('#month-picker');


// shows list of months when month is clicked
monthPicker.onclick = () => {
    monthList.classList.add('show')
};


let workingDayData = {};

// Fetch workingDays data from API and use handleWorkingDays() to generate calendar based on workingDays
fetch('/workingDay')
    .then(response => {

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        handleWorkingDays(data);
    })
    .catch(error => {
        console.error('Error fetching workingDays:', error);
    });


//Generate calendar based on workingDays
const handleWorkingDays = (workingDays) => {
    workingDays.forEach(workingDay => {
        const { date, open } = workingDay; // Destructure date and Open properties from WorkingDayDto
        const dateKey = date;
        workingDayData[dateKey] = open;
    });

    // After processing workingDays data, generate the calendar
    generateCalendar(currMonth.value, currYear.value);
};


// generates a calendar using a month and a year as parameters
generateCalendar = (month, year) => {
    let calendarDay = document.querySelector('.calendar-day');
    calendarDay.innerHTML = '';

    let calendarHeaderYear = document.querySelector('#year');
    let daysOfMonth = [31, getFebDays(year), 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
    let currDate = new Date();

    monthPicker.innerHTML = monthNames[month];
    calendarHeaderYear.innerHTML = year;

    let firstDay = new Date(year, month, 1);


    for (let i = 0; i < daysOfMonth[month] + firstDay.getDay(); i++) {
        let day = document.createElement('div');

        if (i >= firstDay.getDay()) {
            let dayNumber = i - firstDay.getDay() + 1;
            day.textContent = dayNumber;
            day.classList.add('calendarDayHover');

            // Ensure consistent dateKey format (YYYY-MM-DD) for comparison
            const formattedMonth = (month + 1).toString().padStart(2, '0'); // Add leading zero if needed(02-04-2024 instead of 2-4-2024)
            const formattedDay = dayNumber.toString().padStart(2, '0'); // Add leading zero if needed
            const dateKey = `${year}-${formattedMonth}-${formattedDay}`;

            const isOpen = workingDayData[dateKey] || false; // Default to false if key not found

            // Add a click event listener to each day element
            day.addEventListener('click', () => {
                handleDayClick(dayNumber, month, year, isOpen);
            });

            // Add styles to the days in the calendar
            if (!isOpen) {
                day.classList.add('closedDay'); // Apply styles for closed days
            } else {
                day.classList.add('openDay'); // Apply styles for open days
            }

            if (dayNumber === currDate.getDate() && year === currDate.getFullYear() && month === currDate.getMonth()) {
                if (!isOpen) {
                    day.classList.add('currClosedDay'); // Apply styles for closed current day
                } else {
                    day.classList.add('currOpenDay'); // Apply styles for open current day
                }
            }
        }

        calendarDay.appendChild(day);
    }
};


// show months list when clicked
let monthList = calendar.querySelector('.month-list');
monthNames.forEach((e, index) => {
    let month = document.createElement('div')
    month.innerHTML = `<div>${e}</div>`
    month.onclick = () => {
        monthList.classList.remove('show')
        currMonth.value = index
        generateCalendar(currMonth.value, currYear.value)
    }
    monthList.appendChild(month)
});

//change year controls
document.querySelector('#prev-year').onclick = () => {
    --currYear.value
    generateCalendar(currMonth.value, currYear.value)
};

document.querySelector('#next-year').onclick = () => {
    ++currYear.value
    generateCalendar(currMonth.value, currYear.value)
};


let currDate = new Date();
let currMonth = { value: currDate.getMonth() };
let currYear = { value: currDate.getFullYear() };
generateCalendar(currMonth.value, currYear.value);


// click on a day to show it's planning
handleDayClick = (day, month, year, isOpen) => {
    updateDayTitle(day, month, year, isOpen);

    //month + 1 because javascript starts counting months at 0 (januari = 0)
    const javaMonth = month + 1;
    //fetch planning endpoint for a certain day
    fetch(`/planning?day=${day}&month=${javaMonth}&year=${year}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            updateUIWithData(data);
        })
        .catch(error => {
            console.error('Error fetching JSON data:', error);
        });
};

// shows the date of which the planning is shown in a title
updateDayTitle = (year, month, day, isOpen) => {
    const shiftsContainer = document.getElementById('shiftsContainer');
    shiftsContainer.innerHTML = '';
    const detailDiv = document.createElement('div');
    detailDiv.classList.add('shiftTitle');
    let openTitle;
    if(isOpen){
        openTitle = "OPEN";
    } else {
        openTitle = "CLOSED";
    }

    const dayString = `${year} /  ${month + 1} / ${day} : ${openTitle}`;
    detailDiv.textContent = dayString;
    shiftsContainer.appendChild(detailDiv);
}

// shows the planningDetails of a certain date in planningDetails
updateUIWithData = (data) => {
    const shiftsContainer = document.getElementById('shiftsContainer');

    if(data.planningDetails != null){
        data.planningDetails.forEach(detail => {
            const detailDiv = document.createElement('div');
            detailDiv.classList.add('shiftDetail');

            //\xa0 = spacing, probably better way?
            const shiftString = `${detail.startTime || 'N/A'} - ${detail.endTime}
            \xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0
            ${detail.staff}`;
            detailDiv.textContent = shiftString;

            shiftsContainer.appendChild(detailDiv);
        });
    }
};


