# flux-android-sample

A Android sample that implements Flux architecture with React and NoSQL

# Architecture
![Gif Sample Flux Architecture](architecture_gif_sample.png)

#App State Example
``` javascript
{
    gifLocalPath: "path",
    gifDownloadFailureMsg: "error",
    gifUrl: "url",
    gifTitle: "string",
    gifState: PAUSED, //PAUSED, LOOPING, DOWNLOADING, DOWNLOADED, NOT_DOWNLOADED
    gifWatched: false
}
```

#Tests

- [Testing fundamentals](https://stuff.mit.edu/afs/sipb/project/android/docs/tools/testing/testing_android.html)
- [Tests The Android Way](https://www.bignerdranch.com/blog/testing-the-android-way/)
- [Espresso Test Life Cycle](https://jabknowsnothing.wordpress.com/2015/11/05/activitytestrule-espressos-test-lifecycle/)
- [Tests with Espresso, Mockito, Dagger 2](https://engineering.circle.com/instrumentation-testing-with-dagger-mockito-and-espresso-f07b5f62a85b#.5rti2kl2e)
- [Espresso Tutorial](http://www.vogella.com/tutorials/AndroidTestingEspresso/article.html#espresso_exercisesimple)
- [Roboeletric vs Android Test Framework](http://stackoverflow.com/questions/18271474/robolectric-vs-android-test-framework)

###Integration tests

- The integration tests is running on top of espresso framework, and using 
mockito to handle mocks.

To setup a CI or run tests in command line use this command:

    adb shell am instrument -w br.com.catbag.giffluxsample.test/android.support.test.runner.AndroidJUnitRunner

#Good Pratices

###Actions Creators

- Pay attention on action creation UI calls that do something async and 
dispatch action, the main action has to be dispatched before other async
created actions, and its not a good practice dispatch two actions on the 
same synchronous cycle.
