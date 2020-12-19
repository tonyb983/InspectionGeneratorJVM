package im.tony.google.extensions

import im.tony.google.extensions.drive.DriveSpaces
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class DriveExtensionsTests : DescribeSpec({
  describe("DriveSpaces Enum Tests") {
    it("Should give the correct string from enum value.") {
      val drive = DriveSpaces.Drive
      val appDataFolder = DriveSpaces.AppDataFolder
      val photos = DriveSpaces.Photos

      "$drive" shouldBe "drive"
      "$appDataFolder" shouldBe "appDataFolder"
      "$photos" shouldBe "photos"
    }
    it("Should correctly combine spaces.") {
      val driveAndPhotos = DriveSpaces.Drive + DriveSpaces.Photos
      driveAndPhotos shouldBe "drive,photos"
      val all = DriveSpaces.All
      all shouldBe "drive,appDataFolder,photos"
      val second = DriveSpaces.All + DriveSpaces.Photos
    }
  }
})
