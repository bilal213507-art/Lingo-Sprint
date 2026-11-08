package com.lingosprint.app;

import android.app.*;
import android.os.*;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.widget.*;
import android.speech.tts.TextToSpeech;
import java.util.*;

public class MainActivity extends Activity {
    final int NAVY = Color.rgb(24, 39, 75), BLUE = Color.rgb(45, 105, 230);
    final int GREEN = Color.rgb(35, 185, 125), BG = Color.rgb(245, 248, 255);
    final int INK = Color.rgb(32, 43, 66), MUTED = Color.rgb(112, 125, 150);
    LinearLayout base, content, nav;
    TextToSpeech tts;
    android.content.SharedPreferences prefs;
    int xp, streak, hearts, questionIndex, correctCount;
    String language = "English";
    boolean answered = false;
    final String[] prompts = {
        "How do you say “Hello” in Spanish?",
        "What does “Gracias” mean?",
        "Choose the correct translation for “Good morning”.",
        "How do you say “Please” in Spanish?",
        "What does “Adiós” mean?"
    };
    final String[][] choices = {
        {"Hola", "Adiós", "Gracias", "Agua"},
        {"Goodbye", "Thank you", "Please", "Good night"},
        {"Buenas noches", "Buenos días", "Hasta luego", "Por favor"},
        {"Perdón", "Por favor", "Sí", "No"},
        {"Hello", "Thank you", "Goodbye", "Please"}
    };
    final int[] answers = {0,1,1,1,2};

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().setStatusBarColor(NAVY);
        getWindow().setNavigationBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        prefs = getSharedPreferences("lingo", MODE_PRIVATE);
        xp = prefs.getInt("xp", 0); streak = prefs.getInt("streak", 1);
        hearts = prefs.getInt("hearts", 5);
        tts = new TextToSpeech(this, status -> {});
        showHome();
    }

    GradientDrawable shape(int color, int radius, int strokeColor) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color); d.setCornerRadius(radius);
        if (strokeColor != 0) d.setStroke(2, strokeColor);
        return d;
    }
    TextView text(String s, int size, int color, boolean bold) {
        TextView t = new TextView(this); t.setText(s); t.setTextSize(size); t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setGravity(Gravity.CENTER_VERTICAL); return t;
    }
    LinearLayout column() { LinearLayout l = new LinearLayout(this); l.setOrientation(1); return l; }
    LinearLayout row() { LinearLayout l = new LinearLayout(this); l.setOrientation(0); l.setGravity(Gravity.CENTER_VERTICAL); return l; }
    void pad(View v, int a) { v.setPadding(a,a,a,a); }
    LinearLayout.LayoutParams lp(int w, int h) { return new LinearLayout.LayoutParams(w,h); }
    LinearLayout.LayoutParams weight() { return new LinearLayout.LayoutParams(0,-2,1); }

    void shell(String title, String subtitle) {
        base = column(); base.setBackgroundColor(BG);
        setContentView(base);
        LinearLayout header = column(); header.setBackground(shape(NAVY,0,0)); header.setPadding(20,18,20,18);
        TextView brand = text("⚡ LINGO SPRINT", 13, Color.rgb(166,205,255), true);
        header.addView(brand);
        TextView h = text(title, 27, Color.WHITE, true); header.addView(h);
        TextView sub = text(subtitle, 14, Color.rgb(218,228,247), false); header.addView(sub);
        base.addView(header, lp(-1,-2));
        ScrollView scroll = new ScrollView(this);
        content = column(); content.setPadding(18,16,18,20); scroll.addView(content);
        base.addView(scroll, new LinearLayout.LayoutParams(-1,0,1));
        nav = row(); nav.setBackgroundColor(Color.WHITE); nav.setPadding(8,7,8,7);
        base.addView(nav, lp(-1,62));
        navItem("⌂\nHome", () -> showHome());
        navItem("▤\nLearn", () -> showLearn());
        navItem("◎\nPractice", () -> showPractice());
        navItem("♙\nProfile", () -> showProfile());
    }
    void navItem(String label, Runnable action) {
        TextView v = text(label, 12, NAVY, true); v.setGravity(Gravity.CENTER);
        nav.addView(v, new LinearLayout.LayoutParams(0,-1,1)); v.setOnClickListener(w -> action.run());
    }
    void card(String title, String body, String emoji, Runnable action) {
        LinearLayout c = column(); c.setPadding(16,14,16,14); c.setBackground(shape(Color.WHITE,22,Color.rgb(228,234,246)));
        TextView top = text(emoji+"  "+title,17,INK,true); c.addView(top);
        TextView desc = text(body,14,MUTED,false); desc.setPadding(0,6,0,0); c.addView(desc);
        LinearLayout.LayoutParams p = lp(-1,-2); p.bottomMargin=12; content.addView(c,p);
        if(action!=null) c.setOnClickListener(v -> action.run());
    }
    void button(String label, Runnable action) {
        TextView b = text(label,16,Color.WHITE,true); b.setGravity(Gravity.CENTER); b.setPadding(14,15,14,15);
        b.setBackground(shape(BLUE,18,0)); LinearLayout.LayoutParams p=lp(-1,-2); p.bottomMargin=12;
        content.addView(b,p); b.setOnClickListener(v -> action.run());
    }
    void smallLabel(String s) { TextView t=text(s,13,MUTED,true); t.setPadding(2,8,2,8); content.addView(t); }
    void save() { prefs.edit().putInt("xp",xp).putInt("streak",streak).putInt("hearts",hearts).apply(); }

    void showHome() {
        shell("Your next language starts here","Small lessons. Big progress.");
        ImageView logoView = new ImageView(this);
        int logoId = getResources().getIdentifier("lingo_sprint_logo", "drawable", getPackageName());
        logoView.setImageResource(logoId);
        logoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        LinearLayout.LayoutParams logoParams = lp(-1, 210);
        logoParams.bottomMargin = 12;
        content.addView(logoView, logoParams);
        LinearLayout stats=row(); stats.setPadding(14,14,14,14); stats.setBackground(shape(Color.WHITE,20,0));
        stat(stats,"⚡",xp+" XP","Total points"); stat(stats,"🔥",streak+" days","Streak"); stat(stats,"❤️",hearts+"/5","Hearts");
        content.addView(stats,lp(-1,-2));
        smallLabel("YOUR LEARNING PATH");
        card("Spanish • Beginner","Unit 1  ·  Greetings and introductions","🇪🇸",()->startLesson());
        card("Daily goal","Complete one short lesson today to keep your streak alive.","🎯",()->startLesson());
        card("Continue learning","5 quick questions • Earn up to 50 XP","🚀",()->startLesson());
        smallLabel("QUICK ACCESS");
        button("Start a 3-minute lesson  →",()->startLesson());
        card("Practice vocabulary","Review words you've already learned.","🧠",()->showPractice());
    }
    void stat(LinearLayout parent,String icon,String value,String label) {
        LinearLayout box=column(); box.setGravity(Gravity.CENTER);
        box.addView(text(icon,21,BLUE,true)); TextView v=text(value,18,INK,true); v.setGravity(Gravity.CENTER); box.addView(v);
        TextView l=text(label,11,MUTED,false); l.setGravity(Gravity.CENTER); box.addView(l);
        parent.addView(box,new LinearLayout.LayoutParams(0,-2,1));
    }
    void showLearn() {
        shell("Choose your learning path","Pick a course and build a daily habit.");
        smallLabel("POPULAR COURSES");
        card("🇪🇸  Spanish","Beginner • 12 units • Greetings, travel, food","START",()->startLesson());
        card("🇫🇷  French","Beginner • 10 units • Everyday conversations","NEW",()->startLesson());
        card("🇸🇦  Arabic","Beginner • 10 units • Useful everyday phrases","NEW",()->startLesson());
        card("🇵🇰  Urdu","Beginner • 8 units • Daily expressions","NEW",()->startLesson());
        card("🇩🇪  German","Beginner • 10 units • Basics and travel","NEW",()->startLesson());
        card("🇮🇹  Italian","Beginner • 8 units • Speak with confidence","NEW",()->startLesson());
        smallLabel("Tip: Start with one course and practice a little every day.");
    }
    void showPractice() {
        shell("Practice zone","Keep your skills fresh with quick activities.");
        card("Flashcards","Review common Spanish words and meanings.","🗂️",()->flashcards());
        card("Listening practice","Listen to a phrase and repeat it aloud.","🎧",()->speakPhrase("Hola, ¿cómo estás?"));
        card("Speaking practice","Hear a phrase, then say it out loud.","🎙️",()->speakPhrase("Me llamo Alex."));
        card("Quick quiz","Test what you remember in five questions.","🧩",()->startLesson());
        card("Writing practice","Type translations in your notebook.","✍️",()->flashcards());
    }
    void showProfile() {
        shell("Your profile","Every sprint makes you stronger.");
        card("⚡  "+xp+" XP earned","Keep learning to collect more points.","🏆",null);
        card("🔥  "+streak+" day streak","Your current daily learning streak.","📅",null);
        card("❤️  "+hearts+" hearts left","Hearts refill in a future online version.","💗",null);
        smallLabel("ACHIEVEMENTS");
        card("First steps","Complete your first lesson.","🥇",null);
        card("Word collector","Practice vocabulary cards.","📚",null);
        card("Sprint starter","Earn 100 XP.","🚀",null);
        button("Reset learning progress",()->new AlertDialog.Builder(this).setTitle("Reset progress?")
            .setMessage("This will clear your XP, streak and hearts.")
            .setNegativeButton("Cancel",null).setPositiveButton("Reset",(d,w)->{
                xp=0;streak=1;hearts=5;save();showProfile();
            }).show());
    }
    void startLesson() { questionIndex=0; correctCount=0; answered=false; showQuestion(); }
    void showQuestion() {
        shell("Spanish • Unit 1","Question "+(questionIndex+1)+" of "+prompts.length+"     ❤️ "+hearts);
        TextView progress=text("━━━━━━━━━━━━━━━━━━━━━━━━",14, GREEN,true); content.addView(progress);
        smallLabel("TRANSLATE THIS PHRASE");
        TextView q=text(prompts[questionIndex],23,INK,true); q.setPadding(0,12,0,20); content.addView(q);
        for(int i=0;i<4;i++) {
            final int choice=i;
            TextView option=text((char)('A'+i)+".   "+choices[questionIndex][i],17,INK,true);
            option.setPadding(16,17,16,17); option.setBackground(shape(Color.WHITE,17,Color.rgb(220,228,242)));
            LinearLayout.LayoutParams p=lp(-1,-2);p.bottomMargin=10;content.addView(option,p);
            option.setOnClickListener(v -> {
                if(answered)return; answered=true;
                if(choice==answers[questionIndex]) { correctCount++; xp+=10; option.setBackground(shape(Color.rgb(220,250,237),17,GREEN)); }
                else { hearts=Math.max(0,hearts-1); option.setBackground(shape(Color.rgb(255,230,230),17,Color.RED)); }
                save();
                TextView result=text(choice==answers[questionIndex]?"✓  Correct! +10 XP":"✕  Not quite. Correct answer: "+choices[questionIndex][answers[questionIndex]],15,choice==answers[questionIndex]?GREEN:Color.RED,true);
                result.setPadding(4,12,4,12);content.addView(result);
                button(questionIndex==prompts.length-1?"Finish lesson":"Continue  →",()->{
                    if(questionIndex<prompts.length-1){questionIndex++;answered=false;showQuestion();}
                    else finishLesson();
                });
            });
        }
        button("🔊  Hear a sample phrase",()->speakPhrase("Hola. Gracias. Buenos días."));
    }
    void finishLesson() {
        save(); shell("Lesson complete!","Great work. Every minute counts.");
        TextView trophy=text("🏆",64,BLUE,true);trophy.setGravity(Gravity.CENTER);content.addView(trophy,lp(-1,100));
        TextView score=text(correctCount+" / "+prompts.length+" correct",25,INK,true);score.setGravity(Gravity.CENTER);content.addView(score);
        TextView earned=text("You earned "+(correctCount*10)+" XP. Total: "+xp+" XP",16,GREEN,true);earned.setGravity(Gravity.CENTER);content.addView(earned);
        button("Back to home",()->showHome());
        button("Practice again",()->startLesson());
    }
    void flashcards() {
        final String[][] words={{"Hola","Hello"},{"Gracias","Thank you"},{"Por favor","Please"},{"Buenos días","Good morning"},{"Adiós","Goodbye"}};
        final int[] idx={0}; shell("Vocabulary flashcards","Tap the card to reveal its meaning.");
        TextView card=text(words[0][0],30,INK,true);card.setGravity(Gravity.CENTER);card.setBackground(shape(Color.WHITE,24,BLUE));content.addView(card,lp(-1,190));
        card.setOnClickListener(v->{String cur=card.getText().toString();card.setText(cur.equals(words[idx[0]][0])?words[idx[0]][1]:words[idx[0]][0]);});
        button("Next word  →",()->{idx[0]=(idx[0]+1)%words.length;card.setText(words[idx[0]][0]);});
        button("Hear pronunciation 🔊",()->speakPhrase(words[idx[0]][0]));
    }
    void speakPhrase(String phrase) {
        if(tts!=null) { tts.setLanguage(Locale.forLanguageTag("es-ES")); tts.speak(phrase,TextToSpeech.QUEUE_FLUSH,null,"lingo_phrase"); }
        Toast.makeText(this,"Listen and repeat: "+phrase,Toast.LENGTH_LONG).show();
    }
    @Override protected void onDestroy(){if(tts!=null){tts.stop();tts.shutdown();}super.onDestroy();}
}
