
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

// Fetch workingDays data from API and use handleWorkingDays() to generate calendar based on workingDays (mark closed days,...)
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
                day.classList.add('closedDay');
            } else {
                day.classList.add('openDay');
            }

            if (dayNumber === currDate.getDate() && year === currDate.getFullYear() && month === currDate.getMonth()) {
                if (!isOpen) {
                    day.classList.add('currClosedDay');
                } else {
                    day.classList.add('currOpenDay');
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
        hideElements();
        monthList.classList.remove('show')
        currMonth.value = index
        generateCalendar(currMonth.value, currYear.value)
    }
    monthList.appendChild(month)
});

//change year controls
document.querySelector('#prev-year').onclick = () => {
    hideElements();
    --currYear.value
    generateCalendar(currMonth.value, currYear.value)
};

document.querySelector('#next-year').onclick = () => {
    hideElements();
    ++currYear.value
    generateCalendar(currMonth.value, currYear.value)
};


// Hide the thymeleafContent when a new date is selected
function hideElements(){
                const thymeleafContent = document.getElementById('thymeleafContent');
                thymeleafContent.innerHTML = '';
                thymeleafContent.style.display = 'none';
}


let currDate = new Date();
let currMonth = { value: currDate.getMonth() };
let currYear = { value: currDate.getFullYear() };
generateCalendar(currMonth.value, currYear.value);


// click on a day to show it's planning
handleDayClick = (day, month, year, isOpen) => {
    updateDayTitle(day, month, year, isOpen);
    hideElements();


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
updateDayTitle = (day, month, year, isOpen) => {
    const shiftsContainer = document.getElementById('shiftsContainer');
    shiftsContainer.innerHTML = '';
    const detailDiv = document.createElement('div');
    detailDiv.classList.add('shiftTitle');

    let openTitle = isOpen ? "OPEN" : "CLOSED";

    const dayString = `${year} /  ${month + 1} / ${day} : ${openTitle}`;
    detailDiv.textContent = dayString;

        const button = document.createElement('button');
            button.textContent = '  EDIT PLANNING';
            button.classList.add('btn', 'btn-primary');
            button.style.marginLeft = '20px';
            button.onclick = () => {
                console.log(year , month, day);
                loadThymeleafTemplate(year, month + 1, day);
            };

            // Append the button to detailDiv
            detailDiv.appendChild(button);

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
            const shiftString = `${detail.startTime || 'FREE'} - ${detail.endTime || 'Not planned'}
            \xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0
            ${detail.staff.firstName} , ${detail.staff.lastName}`;
            detailDiv.textContent = shiftString;

            shiftsContainer.appendChild(detailDiv);
        });
    }
};


// Function to load Thymeleaf fragment to edit or add planning details
loadThymeleafTemplate = (year, month, day) => {
    const thymeleafContent = document.getElementById('thymeleafContent');
    const url = `/planningDetails/edit?year=${year}&month=${month}&day=${day}`;

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.text();
        })
        .then(data => {
            thymeleafContent.innerHTML = data;
            thymeleafContent.style.display = 'block';
        })
        .catch(error => {
            console.error('There has been a problem with your fetch operation:', error);
            thymeleafContent.innerHTML = `<p>Error loading content: ${error.message}</p>`;
            thymeleafContent.style.display = 'block';
        });
};
