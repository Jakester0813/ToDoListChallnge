**Your To Do Lists**

**Your To Do Lists** is an android app that allows building a todo list and basic todo items management 
functionality including adding new items, editing and deleting an existing item. You
can also add multiple lists, as well as editing the lists and deleting the lists.

Submitted by: **Jake Rushing**

Time spent: **35+** hours spent in total


The following **required** functionality is completed**:

* [x] User can **successfully add and remove items** from the todo list
* [x] User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
* [x] User can **persist todo items** and retrieve them properly on app restart

The following **optional** features are implemented:

* [X] Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [X] Improve style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [X] Add support for completion due dates for todo items (and display within listview item)
* [X] Use a [DialogFragment](http://guides.codepath.com/android/Using-DialogFragment) instead of new Activity for editing items
* [X] Add support for selecting the priority of each todo item (and display in listview item)
* [X] Tweak the style improving the UI / UX, play with colors, images or backgrounds

The following **additional** features are implemented:

* [x] Added the ability to **add multiple To Do Lists**
* [x] Added the ability **Edit your To Do Lists one at a time** as well as **deleting the to do list**
* [x] Added the functionality of **adding the Item to your Google Calendar** after setting the completion date for the item. 
* [x] Added functionality of the To Do List to rearrange items **after adding priority to the item 
      according to priority.** High priority items (or first added priority items that aren't high) 
      get added on top. Medium priority items get added above Low priority, and below High priority 
      (if any high priority items exist), and Low priority items get added below Medium items, and 
      above items that are not priority-assigned.

**Sidenote all of these boxes for required, optional, and additional features were supposed to be 
  marked done, I am not sure why they are not showing up in Readme document. EDIT: Never mind they show up on Github.**



## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='http://i.imgur.com/eg66zL1.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Project Analysis

As part of your pre-work submission, please reflect on the app and answer the following questions below:

**Question 1:** "What are your reactions to the Android app development platform so far? Compare and contrast Android's approach to layouts and user interfaces in past platforms you've used."

**Answer:** Android Development has always been my bread and butter, so I have nothing to contrast too in terms of development for previous platforms.

**Question 2:** "Take a moment to reflect on the `ArrayAdapter` used in your pre-work. How would you describe an adapter in this context and what is its function in Android? Why do you think the adapter is important? Explain the purpose of the `convertView` in the `getView` method of the `ArrayAdapter`."

**Answer:** I wasn't aware that ArrayAdapter is required for the app, I just used RecyclerView for the app. But from what I remember, ArrayAdapters help display items that are set in the ListView after being passed in ArrayAdapter. ConvertView is used to reuse old views that were already passed in.

## Notes

I had never done SQLite before I worked on this app (as I have always wanted to learn more about SQLite implementation), so learning more about implementing SQL was really hard. I also was very nitpicky about saving data so debugging when the lists weren't updated when they were supposed to was a big pain to debug, but I am glad that things work like they should. Last but not least, I never had to implement RecyclerViews with the layout manager set to horizontal (as shown in settings) so learning how to make it work was a ton of fun.

## License

    Copyright [2017] [Apache]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.