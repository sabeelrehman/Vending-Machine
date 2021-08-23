package com.techelevator;

import com.techelevator.view.Menu;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class VendingMachineTest {
    private ByteArrayInputStream input;

    VendingMachineCLI vendingMachineCLI;

    @Before public void setup() {

        vendingMachineCLI = new VendingMachineCLI();
        input = new ByteArrayInputStream("C4".getBytes());
        System.setIn(input);
        vendingMachineCLI.currentBalance = 12.35;

    }

    @Test
    public void shouldReturnSomething() {
        // Arrange
        int[] expectedResult = {0, 49, 1, 0};

        // Act
        int[] actualResult = vendingMachineCLI.subMenuOption3();

        // Assert
        Assert.assertArrayEquals(expectedResult, actualResult);
    }

    @Test
    public void shouldReturn() {
       // Arrange
       String expectedResult = "Heavy|1.50|Drink";

       // Act
       String actualResult = vendingMachineCLI.subMenuOption2();

       // Assert
       Assert.assertEquals(expectedResult, actualResult);
    }
@Test
    public void shouldReturnCrunchCrunch(){
        //Arrange
    String snackType = "Chip";
    String expectedResult = "Crunch Crunch";

    //Act
    String actualResult = vendingMachineCLI.subMenuOption2(snackType.valueOf());

    //Assert

    Assert.assertEquals(expectedResult, actualResult);

}
}
