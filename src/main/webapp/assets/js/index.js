document.addEventListener('DOMContentLoaded', function () {
    // Countdown timer functionality
    function updateCountdown() {
        // Set sale end date
        const countDownDate = new Date();
        countDownDate.setDate(countDownDate.getDate() + 5);

        // Update every second
        const x = setInterval(function () {
            const now = new Date().getTime();
            const distance = countDownDate - now;

            // Calculate time
            const days = Math.floor(distance / (1000 * 60 * 60 * 24));
            const hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            const minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
            const seconds = Math.floor((distance % (1000 * 60)) / 1000);

            // Update DOM safely
            const daysEl = document.getElementById('days');
            const hoursEl = document.getElementById('hours');
            const minutesEl = document.getElementById('minutes');
            const secondsEl = document.getElementById('seconds');

            if (daysEl && hoursEl && minutesEl && secondsEl) {
                daysEl.textContent = days < 10 ? '0' + days : days;
                hoursEl.textContent = hours < 10 ? '0' + hours : hours;
                minutesEl.textContent = minutes < 10 ? '0' + minutes : minutes;
                secondsEl.textContent = seconds < 10 ? '0' + seconds : seconds;
            }

            // Stop when expired
            if (distance < 0) {
                clearInterval(x);
                if (daysEl && hoursEl && minutesEl && secondsEl) {
                    daysEl.textContent = '00';
                    hoursEl.textContent = '00';
                    minutesEl.textContent = '00';
                    secondsEl.textContent = '00';
                }
            }
        }, 1000);
    }

    // Start countdown
    updateCountdown();
});