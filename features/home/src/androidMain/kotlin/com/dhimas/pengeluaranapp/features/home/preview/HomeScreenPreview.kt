package com.dhimas.pengeluaranapp.features.home.preview

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.dhimas.pengeluaranapp.features.home.impl.Expense
import com.dhimas.pengeluaranapp.features.home.impl.HomeScreenContent
import com.dhimas.pengeluaranapp.features.home.impl.HomeScreenState

@Preview(
    name = "Home Screen - Light Mode",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun HomeScreenPreviewLight() {
    HomeScreenContent(
        state = HomeScreenState(
            totalThisMonth = "$157.50",
            expenses = listOf(
                Expense("Groceries", "$50.00", "Food"),
                Expense("Gas", "$30.00", "Transportation"),
                Expense("Coffee", "$5.50", "Food"),
                Expense("Movie Ticket", "$12.00", "Entertainment"),
                Expense("Dinner", "$45.00", "Food"),
                Expense("Uber Ride", "$15.00", "Transportation")
            )
        ),
        onLogoutClick = {},
        onAddExpenseClick = {}
    )
}

@Preview(
    name = "Home Screen - Dark Mode",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun HomeScreenPreviewDark() {
    HomeScreenContent(
        state = HomeScreenState(
            totalThisMonth = "$157.50",
            expenses = listOf(
                Expense("Groceries", "$50.00", "Food"),
                Expense("Gas", "$30.00", "Transportation"),
                Expense("Coffee", "$5.50", "Food"),
                Expense("Movie Ticket", "$12.00", "Entertainment")
            )
        ),
        onLogoutClick = {},
        onAddExpenseClick = {}
    )
}

@Preview(
    name = "Home Screen - Tablet",
    showBackground = true,
    device = "spec:width=1280dp,height=800dp,dpi=240"
)
@Composable
fun HomeScreenPreviewTablet() {
    HomeScreenContent(
        state = HomeScreenState(
            totalThisMonth = "$328.75",
            expenses = listOf(
                Expense("Groceries", "$50.00", "Food"),
                Expense("Gas", "$30.00", "Transportation"),
                Expense("Coffee", "$5.50", "Food"),
                Expense("Movie Ticket", "$12.00", "Entertainment"),
                Expense("Dinner", "$45.00", "Food"),
                Expense("Uber Ride", "$15.00", "Transportation"),
                Expense("Gym Membership", "$50.00", "Health"),
                Expense("Books", "$25.00", "Education")
            )
        ),
        onLogoutClick = {},
        onAddExpenseClick = {}
    )
}

@Preview(
    name = "Home Screen - Empty State",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun HomeScreenPreviewEmpty() {
    HomeScreenContent(
        state = HomeScreenState(
            totalThisMonth = "$0.00",
            expenses = emptyList()
        ),
        onLogoutClick = {},
        onAddExpenseClick = {}
    )
}

@Preview(
    name = "Home Screen - Loading",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun HomeScreenPreviewLoading() {
    HomeScreenContent(
        state = HomeScreenState(
            isLoading = true
        ),
        onLogoutClick = {},
        onAddExpenseClick = {}
    )
}

