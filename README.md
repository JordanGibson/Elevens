# Software Dev. 2 - Assignment 2

Alan Simpson B00777606

Jordan Gibson B00778369

Liam Riche B00789989


# Design & Development 

We decided to use GitHub for our project as this was easy to assign ticket to so that we would know what everyone is doing and working on.

We had a 4 column list that we would use:

The way this works is that a ticket is first opened and assigned for someone to work on then once it has been assigned it is then moved to development section then once it is developed it is then moved to the review stage where the other 2 members of the group review and then either push it up to the master branch or comment on some changes to be made then once it has been finalised it is then moved to the done section where all the completed tickets go. This is how we planned out the development of the project.

You can see the whole preparation, plan, development and testing over at our git hub - [https://github.com/JordanGibson/Elevens](https://github.com/JordanGibson/Elevens)

from this you will also be able to see the pull requests with all the code we have added as well as the reasons as to why we have chosen to create the project like this

## The Deck

The deck – we decided to go and develop an abstract stack data structure as we felt a a stack is the best way to hold a deck as it is first in last out. This way it will make it easier to shuffle the deck as well as keep popping from the top of the deck

## Project Structure

The project Structure -

## Testing

The testing – we have done a couple tests such as DeckTest, InputTest and a general GameTest all of these are their own classes with a test collection in the project so that you are able to look at the different methods with their own tests in the project.

#### UserInput test

the userinput tests are used to find problems in the users inputs into the game (e.g. picking pairs and triplets)
has 4 test categories

these tests cover:
* ensuring a user input will return one of 9 different enums for informing the user of correct inputs (10 tests)
* only sets first and second card but not the third card when valid2card enum is returned (1 test)
* sets all 3 cards if valid3card enum is returned (1 test)
* sanitises inputs from user to remove all spaces and commas and raises input to uppercase (13 tests)

#### Deck test

the decktest tests cover cases to show that the deck class functions as a normal deck of cards would.
has 4 test categories in it 

these tests cover:
* Deck has 52 cards in it (no jokers we don't like jokers)
* when drawing a card it removes one from the stack and doesnt take more or less than 1 card.
* deck does not have duplicate cards in it (a full deck of 52 king of clubs does not make the game play well)
* deck is shuffled on initialisation (makes each game different)

#### Card Test

there is only one test in card test, which checks to make sure the tostring() method correctly returns the name of the card chosen.



