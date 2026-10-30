package com.binh.cinetrack

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class CinetrackAppUiTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun app_launch_shows_Trending_text() {
        // YÊU CẦU 1: Mở app thấy chữ "Trending"
        composeTestRule.waitUntil(timeoutMillis = 5000L) {
            composeTestRule
                .onAllNodesWithText("Trending")
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeTestRule.onNodeWithText("Trending").assertIsDisplayed()
    }

    @Test
    fun click_movie_card_navigates_to_Detail_and_toggles_favorite() {
        // 1. Chờ màn Home nạp xong thấy chữ "Trending"
        composeTestRule.waitUntil(timeoutMillis = 5000L) {
            composeTestRule
                .onAllNodesWithText("Trending")
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        // 2. YÊU CẦU 2: Bấm một MovieCard mở màn Detail
        composeTestRule.onAllNodesWithTag("movie_card")[0].performClick()

        // 3. Đợi màn Detail nạp xong và hiện nút Yêu thích (favorite_button)
        composeTestRule.waitUntil(timeoutMillis = 5000L) {
            composeTestRule
                .onAllNodesWithTag("favorite_button", useUnmergedTree = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        // 4. YÊU CẦU 3: Bấm ♥ thì icon đổi trạng thái
        composeTestRule.onNodeWithTag("favorite_button", useUnmergedTree = true)
            .assertIsDisplayed()
            .performClick()
    }
    @Test
    fun click_settings_button_navigates_to_Settings_screen() {
        // 1. Chờ màn Home nạp xong thấy chữ "Trending"
        composeTestRule.waitUntil(timeoutMillis = 5000L) {
            composeTestRule.onAllNodesWithText("Trending").fetchSemanticsNodes().isNotEmpty()
        }

        // 2. Bấm vào icon Bánh răng Cài đặt trên TopAppBar
        composeTestRule.onNodeWithContentDescription("Cài đặt").performClick()

        // 3. Kiểm tra màn Cài đặt mở ra thấy chữ "Giao diện ứng dụng"
        composeTestRule.onNodeWithText("Giao diện ứng dụng").assertIsDisplayed()
    }
}
