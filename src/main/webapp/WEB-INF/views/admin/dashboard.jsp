<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- Font Awesome for icons -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<!-- Google Fonts for Inter -->
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">
<!-- Chart.js for sales chart -->
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.4/dist/chart.umd.min.js"></script>
<!-- Custom dashboard CSS -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">

<div class="container">
    <!-- Display success or error messages -->
    <c:if test="${not empty success}">
        <div class="message success">
            <i class="fas fa-check-circle"></i> <c:out value="${success}"/>
        </div>
        <c:remove var="success" scope="request"/>
    </c:if>
    <c:if test="${not empty error}">
        <div class="message error">
            <i class="fas fa-exclamation-circle"></i> <c:out value="${error}"/>
        </div>
        <c:remove var="error" scope="request"/>
    </c:if>

    <!-- Dashboard title -->
    <h2><i class="fas fa-tachometer-alt"></i> Admin Dashboard</h2>
    
    <!-- Stats cards section -->
    <div class="stats-cards">
        <!-- Total Products card -->
        <div class="card">
            <i class="fas fa-box card-icon"></i>
            <div class="card-content">
                <span class="card-label">Total Products</span>
                <div class="card-value">${totalProducts}</div>
            </div>
        </div>
        <!-- Total Orders card -->
        <div class="card">
            <i class="fas fa-shopping-cart card-icon"></i>
            <div class="card-content">
                <span class="card-label">Total Orders</span>
                <div class="card-value">${totalOrders}</div>
            </div>
        </div>
        <!-- Total Users card -->
        <div class="card">
            <i class="fas fa-users card-icon"></i>
            <div class="card-content">
                <span class="card-label">Total Users</span>
                <div class="card-value">${totalUsers}</div>
            </div>
        </div>
        <!-- Total Revenue card -->
        <div class="card">
            <i class="fas fa-dollar-sign card-icon"></i>
            <div class="card-content">
                <span class="card-label">Total Revenue</span>
                <div class="card-value">$${totalRevenue}</div>
            </div>
        </div>
    </div>

    <!-- Monthly sales chart section -->
    <div class="chart-section">
        <h3><i class="fas fa-chart-bar"></i> Monthly Sales Overview</h3>
        <div class="chart-container">
            <canvas id="salesChart"></canvas>
        </div>
    </div>
</div>

<script>
    // Initialize Chart.js bar chart for monthly sales
    document.addEventListener('DOMContentLoaded', function() {
        const salesChart = document.getElementById('salesChart').getContext('2d');
        
        // Parse month labels and revenue data from JSP attributes
        const monthLabels = ${monthLabels}; // e.g., ["Jan", "Feb", ...]
        const monthlyRevenue = ${monthlyRevenue}; // e.g., [1000, 2000, ...]

        new Chart(salesChart, {
            type: 'bar',
            data: {
                labels: monthLabels,
                datasets: [{
                    label: 'Monthly Revenue ($)',
                    data: monthlyRevenue,
                    backgroundColor: 'rgba(0, 86, 179, 0.6)', // Matches --primary-color gradient
                    borderColor: 'rgba(0, 86, 179, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: true,
                scales: {
                    y: {
                        beginAtZero: true,
                        grid: {
                            borderDash: [2, 4]
                        },
                        ticks: {
                            callback: function(value) {
                                return '$' + value.toLocaleString();
                            }
                        }
                    },
                    x: {
                        grid: {
                            display: false
                        }
                    }
                },
                plugins: {
                    legend: {
                        display: true,
                        position: 'top',
                        labels: {
                            font: {
                                family: 'Inter',
                                size: 12
                            }
                        }
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                let label = context.dataset.label || '';
                                if (label) {
                                    label += ': ';
                                }
                                if (context.parsed.y !== null) {
                                    label += new Intl.NumberFormat('en-US', {
                                        style: 'currency',
                                        currency: 'USD'
                                    }).format(context.parsed.y);
                                }
                                return label;
                            }
                        }
                    }
                }
            }
        });
    });
</script>