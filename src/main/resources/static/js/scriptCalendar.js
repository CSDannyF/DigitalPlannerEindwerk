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

monthPicker.onclick = () => {
    monthList.classList.add('show')
};




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

            day.classList.add('calendarDayHover')
            day.innerHTML = i - firstDay.getDay() + 1
            day.innerHTML += `<span></span>
                             <span></span>
                             <span></span>
                             <span></span>`

            // Add a click event listener to each day element
            day.addEventListener('click', () => {

                handleDayClick(dayNumber, month, year);
            });




//
//
//            let date = `${year}-${month + 1}-${dayNumber}`;
//            console.log(date);
//            const response = fetch(`/workingDay?date=${date}`);
//            console.log(response);
//
//









            if (dayNumber === currDate.getDate() && year === currDate.getFullYear() && month === currDate.getMonth()) {
                day.classList.add('currDate');
            }
        }

        calendarDay.appendChild(day);
    }
};


// show months when clicked
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
handleDayClick = (day, month, year) => {
    //month + 1 because javascript starts counting months at 0 (januari = 0)
    const javaMonth = month + 1;
    fetch(`/planning?day=${day}&month=${javaMonth}&year=${year}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            updateDayTitle(day, month, year);
            updateUIWithData(data);
        })
        .catch(error => {
            console.error('Error fetching JSON data:', error);
        });
};

// shows the date of which the planning is shown in a title
updateDayTitle = (year, month, day) => {
    const shiftsContainer = document.getElementById('shiftsContainer');
    shiftsContainer.innerHTML = '';
    const detailDiv = document.createElement('div');
    detailDiv.classList.add('shiftTitle');

    const dayString = `${year} /  ${month + 1} / ${day}`;
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



