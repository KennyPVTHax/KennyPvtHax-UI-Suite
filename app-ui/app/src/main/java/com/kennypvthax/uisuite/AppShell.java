package com.kennypvthax.uisuite;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.text.InputFilter;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import java.security.GeneralSecurityException;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Actual app pages. Original Aurora instance survives local page navigation. */
final class AppShell extends FrameLayout implements AutoCloseable {
    private final Activity activity;
    private final SharedPreferences preferences;
    private final AuroraBackground aurora;
    private final TapFeedback sound;
    private final CredentialVault vault;
    private final AuthController auth;
    private final GameActions games;
    private final boolean debug, pendingFixture;
    private final Handler main = new Handler(Looper.getMainLooper());
    private final ExecutorService io = Executors.newSingleThreadExecutor();
    private final ScrollView scroll;
    private final LinearLayout content;
    private LicenseField key;
    private Button save, forget, signIn, cancel, language;
    private TextView status;
    private LoadingMark spinner;
    private Dialog dialog;
    private boolean closed, ioBusy, frozen, dashboard;
    private int pageGeneration;
    private TextView dashboardStatus;
    private final GameCard[] cards=new GameCard[4];
    private int storageMessage;
    private boolean restorePreview;

    AppShell(Activity owner, boolean isDebug) {
        super(owner);
        activity=owner; debug=isDebug;
        pendingFixture=debug&&"pending".equals(owner.getIntent().getStringExtra("auth_fixture"));
        preferences=owner.getSharedPreferences("kenny-ui-suite",0);
        vault=new CredentialVault(owner);
        auth=new AuthController(pendingFixture?new DebugPendingAuthProvider():new UnconfiguredAuthProvider());
        games=new AndroidGameActions(owner);
        sound=new TapFeedback(owner);
        sound.enabled=preferences.getBoolean("sounds",true);
        KennyComponents.reducedMotion=preferences.getBoolean("reduced",false);
        setBackgroundColor(Tokens.BG);
        setLayoutDirection(getResources().getConfiguration().getLayoutDirection());
        aurora=new AuroraBackground(owner);
        aurora.setReduced(KennyComponents.reducedMotion);
        addView(aurora,new LayoutParams(-1,-1));
        scroll=new ScrollView(owner);
        scroll.setFillViewport(true);
        scroll.setClipToPadding(false);
        addView(scroll,new LayoutParams(-1,-1));
        content=new LinearLayout(owner);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(20),dp(16),dp(20),dp(28));
        scroll.addView(content,new ScrollView.LayoutParams(-1,-2));
        setOnApplyWindowInsetsListener((v,insets)->{
            setPadding(insets.getSystemWindowInsetLeft(),insets.getSystemWindowInsetTop(),
                    insets.getSystemWindowInsetRight(),insets.getSystemWindowInsetBottom());
            return insets;
        });
        showLogin();
        auth.observe(()->main.post(()->{if(!closed)authChanged();}));
        readSavedKey();
    }
    private int dp(float value){return (int)KennyComponents.dp(getContext(),value);}
    private String str(int id){return getContext().getString(id);}
    private void space(LinearLayout parent,int amount){View v=new View(getContext());parent.addView(v,new LinearLayout.LayoutParams(1,dp(amount)));}
    private TextView text(LinearLayout parent,int id,int size,int color){
        TextView view=new TextView(getContext());view.setText(id);view.setTextSize(size);view.setTextColor(color);
        view.setTextDirection(TEXT_DIRECTION_FIRST_STRONG);view.setPadding(0,dp(3),0,dp(5));
        parent.addView(view,new LinearLayout.LayoutParams(-1,-2));return view;
    }
    private Button button(int text,int id,Runnable action){
        Button b=KennyComponents.button(getContext(),str(text),()->{sound.play();action.run();});b.setId(id);return b;
    }
    private void full(LinearLayout parent,View child){
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.topMargin=dp(10);parent.addView(child,lp);
    }
    private void header(){
        LinearLayout row=new LinearLayout(getContext());row.setGravity(Gravity.CENTER_VERTICAL);content.addView(row,new LinearLayout.LayoutParams(-1,dp(60)));
        BrandMark logo=new BrandMark(getContext());logo.setId(R.id.header_logo);logo.setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_YES);row.addView(logo,new LinearLayout.LayoutParams(dp(44),dp(44)));
        LinearLayout words=new LinearLayout(getContext());words.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams wordParams=new LinearLayout.LayoutParams(0,-2,1);wordParams.setMarginStart(dp(12));row.addView(words,wordParams);
        TextView title=text(words,R.string.brand,21,Tokens.TEXT);title.setTextDirection(TEXT_DIRECTION_LTR);
        text(words,R.string.identity,10,Tokens.SELECTED);
        TextView local=new TextView(getContext());local.setText(R.string.local);local.setTextColor(Tokens.SECONDARY);local.setTextSize(11);
        local.setPadding(dp(10),dp(6),dp(10),dp(6));local.setBackground(KennyComponents.surface(getContext(),0x99100b0e,Tokens.OUTLINE,20));row.addView(local);
        language=button(R.string.language_button,R.id.language,this::languageDialog);full(content,language);
        space(content,16);
    }
    private void brandPanel(){
        LinearLayout hero=KennyComponents.panel(getContext());hero.setId(R.id.main_brand);hero.setGravity(Gravity.CENTER);
        BrandMark logo=new BrandMark(getContext());hero.addView(logo,new LinearLayout.LayoutParams(dp(88),dp(88)));
        TextView name=text(hero,R.string.brand,28,Tokens.TEXT);name.setGravity(Gravity.CENTER);name.setTextDirection(TEXT_DIRECTION_LTR);
        TextView caption=text(hero,R.string.identity,11,Tokens.SELECTED);caption.setGravity(Gravity.CENTER);caption.setLetterSpacing("en".equals(LocaleSettings.current(activity))?.16f:0);
        content.addView(hero,new LinearLayout.LayoutParams(-1,-2));space(content,18);
    }
    private void showLogin(){
        dashboard=false;++pageGeneration;scroll.scrollTo(0,0);content.removeAllViews();content.setId(R.id.login_screen);header();brandPanel();
        text(content,R.string.welcome,24,Tokens.TEXT);space(content,10);
        LinearLayout panel=KennyComponents.panel(getContext());content.addView(panel,new LinearLayout.LayoutParams(-1,-2));
        text(panel,R.string.key_prompt,14,Tokens.SECONDARY);
        key=new LicenseField(getContext());key.setId(R.id.login_key);key.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4096)});
        LinearLayout.LayoutParams fieldParams=new LinearLayout.LayoutParams(-1,dp(56));fieldParams.topMargin=dp(12);panel.addView(key,fieldParams);
        key.setOnEditorActionListener((v,action,event)->{if(action==android.view.inputmethod.EditorInfo.IME_ACTION_DONE){sound.play();submit();return true;}return false;});
        LinearLayout actions=new LinearLayout(getContext());
        save=button(R.string.save_key,R.id.save_key,()->store(false));
        forget=button(R.string.forget_key,R.id.forget_key,()->store(true));
        LinearLayout.LayoutParams half=new LinearLayout.LayoutParams(0,dp(50),1);half.setMarginEnd(dp(6));actions.addView(save,half);
        actions.addView(forget,new LinearLayout.LayoutParams(0,dp(50),1));full(panel,actions);
        signIn=button(R.string.sign_in,R.id.sign_in,this::submit);signIn.setTag("illuminated");
        cancel=button(R.string.cancel,R.id.auth_cancel,auth::cancel);cancel.setVisibility(INVISIBLE);
        FrameLayout submitSlot=new FrameLayout(getContext());submitSlot.addView(signIn,new FrameLayout.LayoutParams(-1,-1));submitSlot.addView(cancel,new FrameLayout.LayoutParams(-1,-1));
        LinearLayout.LayoutParams submitParams=new LinearLayout.LayoutParams(-1,dp(50));submitParams.topMargin=dp(10);panel.addView(submitSlot,submitParams);
        LinearLayout statusRow=new LinearLayout(getContext());statusRow.setGravity(Gravity.CENTER_VERTICAL);statusRow.setMinimumHeight(dp(96));
        spinner=new LoadingMark(getContext());spinner.setId(R.id.auth_loading);spinner.setVisibility(INVISIBLE);statusRow.addView(spinner,new LinearLayout.LayoutParams(dp(28),dp(28)));
        status=new TextView(getContext());status.setId(R.id.auth_status);status.setTextColor(Tokens.SECONDARY);status.setTextSize(13);status.setAccessibilityLiveRegion(ACCESSIBILITY_LIVE_REGION_POLITE);
        LinearLayout.LayoutParams messageParams=new LinearLayout.LayoutParams(0,-2,1);messageParams.setMarginStart(dp(10));statusRow.addView(status,messageParams);full(panel,statusRow);

        text(content,pendingFixture?R.string.fixture_notice:R.string.backend_notice,12,Tokens.SELECTED);
        if(debug)full(content,button(R.string.preview_dashboard,R.id.preview_dashboard,this::enterPreview));
        full(content,button(R.string.app_preferences,R.id.app_preferences,this::appearanceDialog));
        text(content,R.string.footer,11,Tokens.SECONDARY);
        content.setFocusableInTouchMode(true);content.requestFocus();updateStatus();
    }
    private void submit(){
        if(ioBusy||auth.phase()==AuthController.Phase.LOADING)return;
        storageMessage=0;
        ((InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(key.getWindowToken(),0);
        char[] value=key.getText().toString().toCharArray();
        try{auth.submit(value);}finally{Arrays.fill(value,'\0');}
        updateStatus();
    }
    private void authChanged(){
        if(auth.phase()==AuthController.Phase.SIGNED_IN){if(!dashboard)showDashboard();}
        else if(dashboard){showLogin();readSavedKey();}
        else updateStatus();
    }
    void restorePreview(){if(debug){if(ioBusy)restorePreview=true;else enterPreview();}}
    void enterPreview(){if(debug&&!ioBusy&&auth.phase()!=AuthController.Phase.LOADING)auth.enterDevelopmentPreview();}
    boolean previewActive(){return debug&&dashboard&&auth.session()!=null&&auth.session().development;}
    boolean back(){if(dashboard){sound.play();auth.logout();return true;}return false;}
    private void showDashboard(){
        if(key!=null)key.setText("");if(spinner!=null)spinner.running(false);
        dashboard=true;++pageGeneration;content.removeAllViews();content.setId(R.id.dashboard_screen);scroll.scrollTo(0,0);
        header();brandPanel();
        text(content,auth.session()!=null&&auth.session().development?R.string.preview_notice:R.string.dashboard_session,12,Tokens.SELECTED);
        text(content,R.string.dashboard_title,24,Tokens.TEXT);space(content,12);
        text(content,R.string.game_variants,18,Tokens.TEXT);text(content,R.string.game_help,13,Tokens.SECONDARY);
        dashboardStatus=text(content,R.string.files_unavailable,12,Tokens.SECONDARY);dashboardStatus.setId(R.id.dashboard_status);dashboardStatus.setAccessibilityLiveRegion(ACCESSIBILITY_LIVE_REGION_POLITE);
        GameCatalog.Edition[] editions=GameCatalog.editions();int[] ids={R.id.game_global,R.id.game_korea,R.id.game_taiwan,R.id.game_vietnam};
        for(int i=0;i<editions.length;i++){
            final GameCatalog.Edition edition=editions[i];GameCard card=new GameCard(getContext(),edition,ids[i],sound,()->{
                if(!games.launch(edition.packageName)){dashboardStatus.setText(R.string.launch_failed);dashboardStatus.announceForAccessibility(str(R.string.launch_failed));}
            });cards[i]=card;full(content,card);
        }
        space(content,12);text(content,R.string.footer,12,Tokens.SECONDARY);
        full(content,button(R.string.app_preferences,R.id.app_preferences,this::appearanceDialog));
        full(content,button(R.string.logout,R.id.logout,auth::logout));text(content,R.string.app_info,11,Tokens.SECONDARY);
        content.setFocusableInTouchMode(true);content.requestFocus();refreshEditions();
    }
    private void refreshEditions(){
        final int generation=pageGeneration;GameCatalog.Edition[] editions=GameCatalog.editions();
        io.execute(()->{
            GameActions.Status[] states=new GameActions.Status[editions.length];
            for(int i=0;i<editions.length;i++)states[i]=games.inspect(editions[i].packageName);
            main.post(()->{if(closed||!dashboard||generation!=pageGeneration)return;for(int i=0;i<states.length;i++)cards[i].apply(states[i]);});
        });
    }
    private void appearanceDialog(){
        dialog=new Dialog(activity);dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LinearLayout panel=KennyComponents.panel(getContext());panel.setId(R.id.preferences_dialog);panel.setBackground(KennyComponents.surface(getContext(),Tokens.ELEVATED,Tokens.OUTLINE,12));
        text(panel,R.string.app_preferences,20,Tokens.TEXT);
        KennyComponents.Check reduced=new KennyComponents.Check(getContext(),str(R.string.reduced_motion));reduced.setId(R.id.reduced_motion);reduced.setChecked(KennyComponents.reducedMotion);
        reduced.setOnClickListener(v->{sound.play();KennyComponents.reducedMotion=reduced.isChecked();preferences.edit().putBoolean("reduced",reduced.isChecked()).apply();aurora.setReduced(reduced.isChecked());if(frozen)aurora.stop();if(spinner!=null)spinner.invalidate();});full(panel,reduced);
        KennyComponents.Check sounds=new KennyComponents.Check(getContext(),str(R.string.ui_sounds));sounds.setId(R.id.tap_sounds);sounds.setChecked(sound.enabled);
        sounds.setOnClickListener(v->{sound.play();sound.enabled=sounds.isChecked();preferences.edit().putBoolean("sounds",sound.enabled).apply();});full(panel,sounds);
        full(panel,button(R.string.cancel,View.NO_ID,()->dialog.dismiss()));dialog.setContentView(panel);dialog.show();
        if(dialog.getWindow()!=null){dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);dialog.getWindow().setLayout(Math.min(getWidth()-dp(32),dp(420)),-2);}
    }
    private int authMessage(){
        switch(auth.error()){
            case KEY_REQUIRED:return R.string.key_required;
            case NOT_CONFIGURED:return R.string.not_configured;
            case CANCELLED:return R.string.cancelled;
            case FAILED:return R.string.auth_failed;
            default:return R.string.ready;
        }
    }
    private void updateStatus(){
        if(dashboard)return;
        boolean loading=auth.phase()==AuthController.Phase.LOADING;
        key.setEnabled(!loading&&!ioBusy);save.setEnabled(!loading&&!ioBusy);forget.setEnabled(!loading&&!ioBusy);signIn.setEnabled(!loading&&!ioBusy);language.setEnabled(!loading&&!ioBusy);
        spinner.setVisibility(loading?VISIBLE:INVISIBLE);spinner.running(loading);
        cancel.setVisibility(loading?VISIBLE:INVISIBLE);signIn.setVisibility(loading?INVISIBLE:VISIBLE);
        status.setText(loading?R.string.loading:ioBusy?R.string.storage_busy:storageMessage!=0?storageMessage:authMessage());
        if(loading||auth.phase()==AuthController.Phase.ERROR||storageMessage!=0)status.post(()->{if(!closed&&status.isAttachedToWindow())status.requestRectangleOnScreen(new android.graphics.Rect(0,0,status.getWidth(),status.getHeight()),KennyComponents.reducedMotion);});
        status.setTextColor((storageMessage==R.string.storage_error||storageMessage==R.string.key_required||(storageMessage==0&&auth.phase()==AuthController.Phase.ERROR))?0xffe5776a:Tokens.SECONDARY);
    }
    private void readSavedKey(){
        ioBusy=true;updateStatus();
        io.execute(()->{
            char[] loaded=null;boolean failed=false;
            try{loaded=vault.load();}catch(GeneralSecurityException|IOException|IllegalArgumentException|ClassCastException|java.security.ProviderException|SecurityException e){failed=true;}
            final char[] value=loaded;final boolean error=failed;
            main.post(()->{
                try{if(closed)return;if(!dashboard&&value!=null&&key.length()==0)key.setText(new String(value));ioBusy=false;if(error)storageMessage=R.string.storage_error;updateStatus();if(restorePreview){restorePreview=false;enterPreview();}}
                finally{if(value!=null)Arrays.fill(value,'\0');}
            });
        });
    }
    private void store(boolean remove){
        if(ioBusy||auth.phase()==AuthController.Phase.LOADING)return;
        char[] value=key.getText().toString().toCharArray();
        if(!remove&&value.length==0){storageMessage=R.string.key_required;updateStatus();return;}
        ioBusy=true;updateStatus();
        io.execute(()->{
            boolean ok=true;
            try{if(remove)vault.forget();else vault.save(value);}catch(GeneralSecurityException|IOException|IllegalArgumentException|java.security.ProviderException|SecurityException e){ok=false;}
            finally{Arrays.fill(value,'\0');}
            final boolean success=ok;
            main.post(()->{if(closed)return;ioBusy=false;storageMessage=success?(remove?R.string.forgotten:R.string.saved):R.string.storage_error;if(remove&&success)key.setText("");updateStatus();});
        });
    }
    private void languageDialog(){
        dialog=new Dialog(activity);dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);dialog.setTitle(R.string.choose_language);
        LinearLayout panel=KennyComponents.panel(getContext());panel.setId(R.id.language_dialog);
        panel.setBackground(KennyComponents.surface(getContext(),Tokens.ELEVATED,Tokens.OUTLINE,12));
        text(panel,R.string.choose_language,21,Tokens.TEXT);
        String[] labels={"English","中文","हिन्दी","العربية"};int[] ids={R.id.language_en,R.id.language_zh,R.id.language_hi,R.id.language_ar};
        for(int index:new int[]{3,0,1,2}){
            KennyComponents.Check choice=new KennyComponents.Check(getContext(),labels[index]);choice.setId(ids[index]);choice.setChecked(LocaleSettings.current(activity).equals(LocaleSettings.LANGUAGES[index]));
            choice.setOnClickListener(v->{sound.play();choice.setChecked(true);if(LocaleSettings.save(activity,LocaleSettings.LANGUAGES[index])){dialog.dismiss();activity.recreate();}});full(panel,choice);
        }
        full(panel,button(R.string.cancel,View.NO_ID,()->dialog.dismiss()));dialog.setContentView(panel);
        if(dialog.getWindow()!=null){dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);}
        dialog.show();if(dialog.getWindow()!=null)dialog.getWindow().setLayout(Math.min(getWidth()-dp(32),dp(420)),-2);
    }
    void resume(){if(!frozen)aurora.start();if(dashboard)refreshEditions();if(spinner!=null)spinner.running(auth.phase()==AuthController.Phase.LOADING);}
    void pause(){aurora.stop();spinner.running(false);if(dialog!=null){dialog.dismiss();dialog=null;}}
    void freezePreview(float seconds){if(!Float.isFinite(seconds)||seconds<0||seconds>60)return;frozen=true;aurora.stop();aurora.model.time=seconds;aurora.model.particleTime=seconds;aurora.invalidate();}
    public boolean dispatchTouchEvent(MotionEvent event){aurora.touch(event.getX()-aurora.getLeft(),event.getY()-aurora.getTop(),event.getActionMasked()!=MotionEvent.ACTION_UP&&event.getActionMasked()!=MotionEvent.ACTION_CANCEL);return super.dispatchTouchEvent(event);}
    public void close(){closed=true;pause();auth.close();io.shutdownNow();if(key!=null)key.setText("");sound.close();}
}
