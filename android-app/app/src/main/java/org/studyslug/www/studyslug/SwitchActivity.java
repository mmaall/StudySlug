package org.studyslug.www.studyslug;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class SwitchActivity {


    public Activity sourceActivity;

    SwitchActivity(Activity activity){
        this.sourceActivity = activity;
    }

    public void SwitchToFindPeople(Activity sourceActivity){
        //TODO:Implement this correctly
         Intent findPeopleIntent = new Intent(sourceActivity,FindPeopleActivity.class);
         sourceActivity.startActivity(findPeopleIntent);

    }
    public void SwitchToAddCourses(Activity sourceActivity){
         Intent addCoursesIntent = new Intent(sourceActivity,AddCoursesActivity.class);
         sourceActivity.startActivity(addCoursesIntent);

    }
}
