MarkTime
========
Author: John "boar401s2" Board
Date: 31/03/2014
Description: App to assit in the administration of a Boy's Brigade Australia company.

This app was originally created to assist in the marking of attendance and uniform completeness
of boys on Boy's Brigade nights. Originally this was marked on paper, but to compile several nights
of data would take a fair amount of time when considering a person for badge requirements or 
promotions.

This app seeks to solve the problem by moving the marking into the digital realm where computers
can calculate the attendance and uniform completeness of people quickly. The convinience of the app
was quickly proven, and soon John was asked to expand it to accomadate other information about Squad
Challenges, what Uniform a person owns, Badges, etc.

This implementation of the app is written in Java for Android. It would upload the information a Google
Spreadsheet. This quickly was proved slow and inefficient. Also the lack of compadability with iOS was
somewhat restricting. This prompted John to decide to re-write the app to use the PhoneGap/Cordova framework.
This will allow him to write the code once in JavaScript, and have it immediatly compadible with all
major platforms.
