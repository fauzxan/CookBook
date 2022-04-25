# CookBook
### An app that helps keep track of the inventory in your fridge, and notifies you of when your products are expiring. 
### Team 4D
1005132  Vincent Chua Yao Sen </br>
1005299  Oakar Min </br>
1005388  Namitha Maria Justine 		 </br>
1005404  Mohammed Fauzaan Mahaboob 	 </br>
1005495  Keshav Natarajan </br>
1005644  Yuliati </br>

## Context and problem statement
We observed from our friends, families and even from ourselves that when we buy our groceries, we use only a small portion of the ingredients and store the remaining in the kitchen. 

After which, we tend to forget the items in our fridge and the next time we check for ingredients, most of them have gone bad, leading to unnecessary food wastage. 

This brings us to our problem statement: How might we reduce the food wastage at home by improving the tracking methods of the ingredients at home?

## Proposed solution
When the users store the groceries, they would scan the barcode or manually input the items into the CookBook. After which, the user would enter the expiry dates of the ingredients into the app. 

CookBook keeps track of the items in the fridge and is designed to send a notification to the user the day before the expiry date of the ingredient.

## Features
### Inventory List
The inventory list is the landing page that the user will be directed to after logging into the app. In this page they will be able to add items that cannot be scanned by the barcode scanner manually into the database. A popup of a calendar will appear whenever they add an item to indicate the expiry date for that item. All added items in the database will appear as a scrollable list in ascending order of expiry date in the page itself. Should the user need to remove any items from the list, they can toggle the “Remove items” switch and tap on the respective items to remove. The nearest expiry date for that item will be removed from the list and the database.
### Notifications
After the users have added their items into the CookBook, our app will send a notification the day before the expiry of all items in the inventory. Through this, the users are reminded to use the ingredients or consume the food items they have in the kitchen and therefore, this ensures that the food is not kept beyond its expiry date and thus,there will be reduced unnecessary food wastage.
We implement the notification using these following classes: 
MainActivity, NotificationReceiver and NotificationHelper. 
### Cart
This page is like a shopping list for the user. The list will be shown on the page and the list is updated automatically when there is an expired item. Users are also allowed to insert items into the cart manually by inputting the item name and quantity on the given section. Furthermore, items can be easily removed by clicking at the “trash bin” icon and selecting the item that user wish to be removed.
### Barcode Scanner
This is an interface that allows users to insert item to inventory item fuss free and efficiently. First, scan the barcode of the item that the user wishes to add into inventory. Next, choose the expiry date of the item using the prompt calendar. And that’s it, the item is added to the inventory list!

## Possible future work
For CookBook to go the extra mile, we could consider collaborating with a grocery firm such as Fairprice or Cold Storage to obtain the barcodes of the items at their shops to make our database more robust. Through this the customers could use the barcode scanner for most of the ingredients into the CookBook app making the app to have better automation and therefore making it easier for the users to use the CookBook app. 

Moreover, we could implement a feature whereby users are not only told what is expiring but what dishes could be made with the ingredients that will spoil. This will relieve the user of any extra effort it might take to find a way to make use of the ingredients. This will also encourage the user to use the ingredients instead of simply throwing it out. The app should also allow the user to input their own recipes to be recommended. 

## Conclusion
Our app aims to reduce the food wastage at home, which is a step in the right direction towards tackling the larger problem of food wastage in general. It helps in keeping track of the items in stock, and their expiry dates. Through improvements in the tracking methods and notifications sent the day before the expiry of the products, the users can have effective use of their ingredients. We believe that the widespread use of CookBook would result in people maximising the utility of their ingredients and at the same time minimising unnecessary wastage. With this principle, we can truly make a better world by design. 
