
# CineTrack - Application xem phim

![CI Status](https://github.com/hghg-ai/cinetrack-journey/actions/workflows/ci.yml/badge.svg)

## 📌 Giới thiệu
CineTrack là ứng dụng di động Android giúp tìm kiếm, theo dõi các bộ phim hot,
phim phổ biến và lưu trữ danh sách phim yêu thích.

## 🛠 Tech Stack & Kiến trúc
- **Kiến trúc:** MVVM + Repository + Single Source of Truth (SSOT) + UDF.
- **Dependency Injection:** Hilt.
- **UI:** Jetpack Compose + Material3.
- **Lưu trữ dữ liệu:** Room Database + DataStore Preferences.
- **Phân trang:** Paging 3 (Popular cuộn vô hạn).
- **Mạng:** Retrofit2 + Kotlinx Serialization.
- **Tác vụ ngầm:** WorkManager + HiltWorker.
- **Testing:** JUnit4, Coroutines Test, Turbine, Compose UI Test.

[video demo](https://drive.google.com/file/d/1QI30WunQhZpOMZWXqzDmZgfEC_OvGbby/view?usp=drive_link)

