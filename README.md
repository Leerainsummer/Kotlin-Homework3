# 電子三甲 113360114 李雨夏

Kotlin Homework3

| 專案 | 實作 |
| --- | --- |
| lab7 | 購物清單，Spinner、GridView、ListView 與自訂 Adapter |
| lab8 | 通訊錄，RecyclerView、新增、刪除與本機保存 |
| lab9_1 | 龜兔賽跑，Coroutines 的 launch 與 delay |
| LAB9_2 | 體位檢測機，輸入驗證、非同步進度與計算結果 |

使用 Android Studio 分別開啟各專案。AGP 9.4.1 內建 Kotlin，Gradle 9.6.0，compileSdk / targetSdk 37，minSdk 24。

非同步實作使用 lifecycleScope。離開並銷毀畫面時協程會取消，重新開始時建立新工作。龜兔賽跑的兩個協程都在 Main dispatcher 更新 UI，delay 暫停期間可處理其他事件。體位檢測先讀取輸入快照，避免檢測期間變更輸入導致結果混用。

Java Thread 是執行緒，Thread.sleep 會阻塞目前執行緒，更新 UI 需要切回主執行緒。Kotlin Coroutine 可透過 delay 暫停並釋放執行緒，Job 可取消，lifecycleScope 可連結畫面生命週期。Coroutine 不保證自動在背景執行，耗時運算或阻塞 IO 仍需選擇合適 dispatcher。

教材來源 https://github.com/taipeitechmmslab/MMSLAB-Android-Kotlin
本作業沿用教材介面、圖片與體位計算公式，並依作業要求將兩個 Lab 9 的 Thread 改成 Coroutines。

教材基準提交 faca2fb8d2fb2e9723eac1442c44fae8350ebe0a。

四個專案均通過 assembleDebug 與 lintDebug。API 37 模擬器已驗證數量選單與水果清單、聯絡人新增/保存/刪除、賽跑完成與重新開始、體位輸入檢查、進度更新與男女計算結果。
