package im.tony.data

import io.kotest.assertions.konform.shouldBeInvalid
import io.kotest.assertions.konform.shouldBeValid
import io.kotest.core.spec.style.DescribeSpec

val testOwner1 by lazy {
  OwnerData(
    "18400GW",
    "Johnson",
    "18400 Gardenia Way",
    "Gaithersburg, MD 20879",
    null,
    null,
  )
}
val testOwner2 by lazy {
  OwnerData(
    "1CJC",
    "Michaelson",
    "1 Cape Jasmine Court",
    "Gaithersburg, MD 20879",
    "701 Monroe St. #203",
    "Rockville, MD 20850",
  )
}
val testOwner3 by lazy {
  OwnerData(
    "0ABC",
    "Satanson",
    "0 Atlanta Bread Court",
    "Wonderland, ZQ 69696",
    "Hey girl heyyyy",
    "Where you get all that from?!",
  )
}

class OwnerDataTests : DescribeSpec({
  describe("OwnerData Tests") {
    describe("Validator Tests") {
      it("Should correctly validate the three test owners.") {
        OwnerDataValidator shouldBeValid testOwner1
        OwnerDataValidator shouldBeValid testOwner2
        OwnerDataValidator shouldBeInvalid testOwner3
      }
    }
  }
})
