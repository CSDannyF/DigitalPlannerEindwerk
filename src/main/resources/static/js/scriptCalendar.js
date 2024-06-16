
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
let status;

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


const handleWorkingDays = (workingDays) => {
    workingDays.forEach(workingDay => {
        const { date, open, planningDto } = workingDay; // Destructure date, open, and planning properties from WorkingDayDto

        const dateKey = date;
        const status = planningDto && planningDto.planningStatusDto ? planningDto.planningStatusDto.planningStatusName : null;

        workingDayData[dateKey] = {
            open: open,
            status: status
        };

    });

    // After processing workingDays data, generate the calendar
    generateCalendar(currMonth.value, currYear.value);
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

            //  dateKey formatting (YYYY-MM-DD)
            const formattedMonth = (month + 1).toString().padStart(2, '0'); // Add leading zero if needed
            const formattedDay = dayNumber.toString().padStart(2, '0');
            const dateKey = `${year}-${formattedMonth}-${formattedDay}`;

            const dayData = workingDayData[dateKey] || {}; // Get the day data

            const isOpen = dayData.open;
            const status = dayData.status;
            const isPlannedNOK = status === "Planned NOK";
            const isPlannedOK = status === "Planned OK";


            // Add a click event listener to each day element
            day.addEventListener('click', () => {
                handleDayClick(dayNumber, month, year, isOpen);
            });

            // Add styles to the days in the calendar
            if (isOpen) {
                day.classList.add('openDay');
            } else {
                day.classList.add('closedDay');
            }

            if (isPlannedNOK) {
                day.classList.add('plannedNOK');
            }

            if (isPlannedOK) {
                console.log('Applying plannedNOK styling');
                day.classList.add('plannedOK');
            }

            if (dayNumber === currDate.getDate() && year === currDate.getFullYear() && month === currDate.getMonth()) {
                if (isOpen) {
                    day.classList.add('currOpenDay');
                } else {
                    day.classList.add('currClosedDay');
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


// Hide the shifts container when a new date is selected
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
            button.textContent = '  ADD SHIFTS';
            button.classList.add('btn', 'btn-primary');
            button.style.marginLeft = '20px';
            button.onclick = () => {
                console.log(year , month, day);
                loadThymeleafTemplate(year, month + 1, day);
            };

            detailDiv.appendChild(button);

    shiftsContainer.appendChild(detailDiv);
}

//check if logged-in user is admin
function isAdmin() {
  const isAdminInput = document.getElementById('isAdmin');
  return isAdminInput.value === 'true';
}


// show shifts on the day that is clicked
updateUIWithData = (data) => {
    const shiftsContainer = document.getElementById('shiftsContainer');

    if (data.planningDetails != null) {
        data.planningDetails.forEach(detail => {

            const detailDiv = document.createElement('div');
            detailDiv.classList.add('shiftDetail', 'mb-1', 'p-1', 'border', 'rounded', 'd-flex', 'justify-content-between', 'align-items-center');

            const nameDiv = document.createElement('div');
            // show staff name, if staff is null, show OPEN SHIFT
            nameDiv.textContent = detail.staff ? `${detail.staff.firstName} , ${detail.staff.lastName}` : 'OPEN SHIFT';
            nameDiv.classList.add('shiftName');


           const timeDiv = document.createElement('div');
            timeDiv.classList.add('shiftTime');

            if (detail.availability && (detail.availability.availabilityType === "Sick" || detail.availability.availabilityType === "Absence")) {
                // Set different text content based on availability type
                timeDiv.textContent = detail.availability.availabilityType; // Set to availability type if "Sick" or "Absence"
                detailDiv.classList.add('unavailable'); // Add class to parent div for styling
            } else {
                timeDiv.textContent = `${detail.startTime || 'FREE'} - ${detail.endTime || 'Not planned'}`;
            }

            // Check if it's an open shift to color it red
            if (!detail.staff) {
                if(isAdmin()){
                    console.log("is admin");
                    detailDiv.classList.add('openShift');
                    const button = document.createElement('button');
                    button.textContent = 'Edit';
                    button.classList.add('btn', 'btn-primary', 'ms-2');
                    button.addEventListener('click', () => {
                        editShift(detail.planningDetailId);
                    });
                    detailDiv.appendChild(button);

                } else {
                    console.log("is not admin");
                    detailDiv.classList.add('openShift');
                    const button = document.createElement('button');
                    button.textContent = 'Assign';
                    button.classList.add('btn', 'btn-primary', 'ms-2');
                    button.addEventListener('click', () => {
                        assignShift(detail.planningDetailId);
                    });
                    detailDiv.appendChild(button);
                }


            } else {
                // Check isAdmin status before creating edit button
                if (isAdmin()) {
                    const editButton = document.createElement('button');
                    editButton.textContent = 'Edit';
                    editButton.classList.add('btn', 'btn-primary', 'ms-2', 'edit-button');
                    editButton.addEventListener('click', () => {
                        editShift(detail.planningDetailId);
                    });
                    detailDiv.appendChild(editButton);
                }
            }
            detailDiv.appendChild(nameDiv);
            detailDiv.appendChild(timeDiv);
            shiftsContainer.appendChild(detailDiv);
        });
    }
};


function editShift(planningDetailId) {
    window.location.href = `/planningDetails/edit?planningDetailId=${planningDetailId}`;
}




// Function to load Thymeleaf fragment to edit or add planning details
loadThymeleafTemplate = (year, month, day) => {
    const thymeleafContent = document.getElementById('thymeleafContent');
    const url = `/planningDetails/add?year=${year}&month=${month}&day=${day}`;

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



// assign a user to an open shift
function assignShift(shiftId) {
    fetch('/assignShift', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ shiftId: shiftId })
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error('Failed to assign shift');
        }
    })
    .then(data => {
        alert(data.message);
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error assigning shift');
    });
}

