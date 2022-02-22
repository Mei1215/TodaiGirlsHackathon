package com.example.eduapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.language.v1.CloudNaturalLanguage;
import com.google.api.services.language.v1.CloudNaturalLanguageRequestInitializer;
import com.google.api.services.language.v1.model.ClassifyTextRequest;
import com.google.api.services.language.v1.model.ClassifyTextResponse;
import com.google.api.services.language.v1.model.Document;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.cloud.translate.v3.LocationName;
//import com.google.cloud.translate.v3.TranslateTextRequest;
//import com.google.cloud.translate.v3.TranslateTextResponse;
//import com.google.cloud.translate.v3.Translation;
//import com.google.cloud.translate.v3.TranslationServiceClient;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.HashMap;
import java.util.Map;


public class PostMainActivity extends AppCompatActivity {

    private static final String API_KEY = "";


//    public static void getSupportedLanguagesForTarget() throws IOException {
//        // TODO(developer): Replace these variables before running the sample.
//        String projectId = "YOUR-PROJECT-ID";
//        // Supported Languages: https://cloud.google.com/translate/docs/languages
//        String languageCode = "your-language-code";
//        getSupportedLanguagesForTarget(projectId, languageCode);
//    }
//
//    // Listing supported languages with target language name
//    public static void getSupportedLanguagesForTarget(String projectId, String languageCode)
//            throws IOException {
//
//        // Initialize client that will be used to send requests. This client only needs to be created
//        // once, and can be reused for multiple requests. After completing all of your requests, call
//        // the "close" method on the client to safely clean up any remaining background resources.
//        try (TranslationServiceClient client = TranslationServiceClient.create()) {
//            // Supported Locations: `global`, [glossary location], or [model location]
//            // Glossaries must be hosted in `us-central1`
//            // Custom Models must use the same location as your model. (us-central1)
//            LocationName parent = LocationName.of(projectId, "global");
//            GetSupportedLanguagesRequest request =
//                    GetSupportedLanguagesRequest.newBuilder()
//                            .setParent(parent.toString())
//                            .setDisplayLanguageCode(languageCode)
//                            .build();
//
//            SupportedLanguages response = client.getSupportedLanguages(request);
//
//            // List language codes of supported languages
//            for (SupportedLanguage language : response.getLanguagesList()) {
//                System.out.printf("Language Code: %s\n", language.getLanguageCode());
//                System.out.printf("Display Name: %s\n", language.getDisplayName());
//            }
//        }
//    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_main);
//        String body = "We could not just stand by and see those injustices of the terrorists, denying our rights, ruthlessly killing people, and misusing the name of Islam. We decided to raise our voice and tell them, “Have you not learned that in the Holy Quran Allah says if you kill one person, it is as if you killed the whole humanity? Do you not know that Muhammad, peace be upon him, the Prophet of mercy, he says, ‘Do not harm yourself or others?’ And do you not know that the very first word of the Holy Quran is the word ‘iqra,’ which means ‘read?'”\n" +
//                "\n" +
//                "The terrorists tried to stop us and attacked me and my friends who are here today, on our school bus, in 2012.But neither their ideas nor bullets could win.\n" +
//                "\n" +
//                "We survived. And since that day, our voices have grown louder and louder.\n" +
//                "\n" +
//                "I tell my story, not because it is unique, but because it is not.\n" +
//                "\n" +
//                "It is the story of many girls.\n" +
//                "\n" +
//                "Today, I tell their stories too. I have brought with me some of my sisters, from Pakistan, from Nigeria and from Syria, who share this story. My brave sisters, Shazia and Kainat who were also shot that day on our school bus, but they have not stopped learning. And my brave sister, Kainat-Somro, who went through severe abuse and extreme violence. Even her brother was killed, but she did not succumb.\n" +
//                "\n" +
//                "Also, my sisiters here, whom I have met during my Malala Fund campaign, my 16-year-old courageous sister, Mezon, From Syria, who now lives in Jordan as a refugee, and she goes from tent to tent encouraging girls and boys to learn.\n" +
//                "\n" +
//                "And my sister Amina, from the north of Nigeria, where Boko Haram threatens and stalks girls and even kidnaps girls, just for wanting to go to school.\n" +
//                "\n" +
//                "Though I appear as one girl, one person, who is 5 foot 2 inches tall, if you include my high heels. It means I am 5 foot only. I am not a lone voice, I am many.";
//        String body = "aaa";


        TextView time = findViewById(R.id.textView6);
        time.setText(getNowDateString());



//        classifyAndPostTopic(body);
        Button b = findViewById(R.id.button);
        b.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        EditText editTextTextPersonName = findViewById(R.id.editTextTextPersonName);
                        String body = editTextTextPersonName.getText().toString();
                        classifyAndPostTopic(body);
                        finish();
                    }
                }
        );

    }

    private void classifyAndPostTopic(String body){
        CloudNaturalLanguage naturalLanguageService = new CloudNaturalLanguage.Builder(
                new NetHttpTransport(),
                new GsonFactory(),
                null
        ).setCloudNaturalLanguageRequestInitializer(
                new CloudNaturalLanguageRequestInitializer(API_KEY)
        ).build();

        ClassifyTextRequest request = new ClassifyTextRequest();
        Document document = new Document();
        request.setDocument(document);
        document.setType("PLAIN_TEXT");
        document.setContent(body);


        new AsyncTask<Object, Void, ClassifyTextResponse>() {
            @Override
            protected ClassifyTextResponse doInBackground(Object... params) {
                ClassifyTextResponse response = null;
                try {
                    response = naturalLanguageService.documents().classifyText(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                    response = null;
                }
                return response;
            }
            @Override
            protected void onPostExecute(ClassifyTextResponse response) {
                super.onPostExecute(response);
                if (response != null) {
                    Log.d("POST",response.getCategories().get(0).getName());
                    postTopic(body, (String)response.getCategories().get(0).getName());
                }else{
                    postTopic(body,"/Warning/The string is too short.");
                }
            }
        }.execute();


    }

    public String getNowDateString() {
        final DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

    private void postTopic(String body, String category){
//        String category = "/Other";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
        Map<String, Object> topic = new HashMap<>();
        topic.put("body", body);//bodyが入る予定
        topic.put("category", category);//categoryが入る予定
        topic.put("created_at", getNowDate());//getNowDateから入手する予定

        // Add a new document with a generated ID
        db.collection("topics")
                .add(topic)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });
    }

    public static Date getNowDate(){
        final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final Date date = new Date(System.currentTimeMillis());
        return date;
    }

}