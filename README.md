#Job Finder app 


in Job finder app we use two providers along with the capibality to add more providers into the the code simply by adding the provider's model and connecting it with a center model called Job.

Restfull solution:
developers usually use a restfull library such as retrofit or volly, In Job finder app i have implemented my own classic solution which is more configrible.
Adding a simple pethod with a diffrent request type should do the trick into my solution it acts as volly which by being multi threaded with AsyncTasks, and a callback method that returns the result so you can do more action into this callback method.

Search:
I have implemented a classic Autocomplete SearchView which is in the toolbar of the app, adter the text is being entered pressed to serch is will call the providers via GET request both apis search similarity in terms of search, but not the keys of the response. the gitHub api is paginated and handled however the search.gov is not due to the lack of apis. 


Google Places api:
the solution is there however it is commented and I did not implement places api to this project because in order for google api places to work google requires a billing account which i currently do not have. however with a little more time it can  be done swiftly.

Extras: 
 the code uses recyclerView as a listing along with its onTouch interface and its own custom adapter.

Libraries that were used:

GSON library which helps me deserialize my models/ retrofit does this automatically but i like my own solution and i am aiming to make it better and faster.

PLEASE FIND CLONE MY REPO USING git clone repository

