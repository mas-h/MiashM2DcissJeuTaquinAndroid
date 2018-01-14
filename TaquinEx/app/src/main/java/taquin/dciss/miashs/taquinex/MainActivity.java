package taquin.dciss.miashs.taquinex;

import android.nfc.Tag;
import android.os.Build;
import android.util.Log;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import android.support.v7.app.ActionBar.DisplayOptions;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.String;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Intent.ACTION_PICK;

public class MainActivity extends AppCompatActivity  {
    static final int REQUEST_TAKE_PHOTO = 1,SELECT_FILE=0;
    public static Uri uriString;
    public static int choice;
    private int level;
    private ImageView imageView;
    String mCurrentPhotoPath;
    Bitmap bitmapImage ;
    private CheckBox checkBox;
    private CheckBox checkBox2;
    private CheckBox checkBox3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // chargement du layout

        imageView = (ImageView) findViewById(R.id.imageView); // définition de la zone où se trouvera l'image d'aperçu avant de démarrer le jeu

        checkBox = (CheckBox) findViewById(R.id.checkBox);
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox) findViewById(R.id.checkBox3);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(checkBox.isChecked()) {
                    checkBox2.setChecked(false);

                    checkBox3.setChecked(false);

                }
                else{
                    checkBox.setChecked(false);

                }
            }
        });
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(checkBox2.isChecked()) {
                    checkBox.setChecked(false);

                    checkBox3.setChecked(false);

                }
                else{
                    checkBox2.setChecked(false);

                }

            }
        });
        checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(checkBox3.isChecked()) {
                    checkBox2.setChecked(false);

                    checkBox.setChecked(false);

                }
                else{
                    checkBox3.setChecked(false);

                }
            }
        });

        // Définition du bouton pour lancer l'appareil photo
        Button capture = (Button) findViewById(R.id.btnCapture);
        capture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        // uriString = this.getIntent().getData();
        //if(uriString==null){


        // Définition du bouton de lancement du jeu, au clique on enregistre la photo
        // et on lance la seconde activité en envoyant le lien de la photo et le niveau de la grille
        Button btnJouer = (Button) findViewById(R.id.BtnJouer);

        btnJouer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (choice == REQUEST_TAKE_PHOTO) {
                    galleryAddPic();
                    //==================================================================================
                    Log.d("Message", "Je suis dans btnJouerCamera " + mCurrentPhotoPath);

                }
                if(choice == SELECT_FILE) {
                    mCurrentPhotoPath= uriString.toString();
                    //==================================================================================
                    Log.d("Message", "Je suis dans btnJouerGallery " + mCurrentPhotoPath);
                }

                Intent intent = new Intent(MainActivity.this, TaquinActivity.class);
                intent.putExtra("choice", getChoice());
                intent.putExtra("mCurrentPhotoPath", mCurrentPhotoPath);
                intent.putExtra("level", level);
                startActivity(intent);
            }
        });
    }

    // Lancement de l'appareil photo
    private void dispatchTakePictureIntent() {

        final CharSequence[] items = {"Camera","Gallery","Cancel"}; // le menu pour choisir entre camera ou gallerie
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {
                    Log.d("CAMERAAAAA","cameraIF");
                    setChoice(REQUEST_TAKE_PHOTO);
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        File photoFile = null;
                        try {
                            photoFile = createImageFile(); // création du fichier
                        } catch (IOException ex) {
                            System.err.println(ex);
                        }
                        if (photoFile != null) {
                            // si le fichier a bien été créé on enregistre la photo dedans
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                            setUriString(Uri.fromFile(photoFile)); // enregistrement de l'uri
                            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO); // on retourne sur la vue d'accueil du jeu (l'activité qui à fait la requête)
                        }
                    }

                } else if (items[i].equals("Gallery")) {
                    setChoice(SELECT_FILE);
                    Intent takePictureIntent = new Intent(ACTION_PICK);
                    takePictureIntent.setType("image/*");
                    Uri uriGall = takePictureIntent.getData();
                    setUriString(uriGall);
                    Log.d("OUHHHHHHHHHHHHHH", ""+uriGall );
                    startActivityForResult(takePictureIntent.createChooser(takePictureIntent, "Select File"), SELECT_FILE);


                }


                else if(items[i].equals("Cancel")){
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();

        /*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile(); // création du fichier
            } catch (IOException ex) {
                System.err.println(ex);
            }
            if (photoFile != null) {
                // si le fichier a bien été créé on enregistre la photo dedans
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                setUriString(Uri.fromFile(photoFile)); // enregistrement de l'uri
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO); // on retourne sur la vue d'accueil du jeu (l'activité qui à fait la requête)
            }
        }*/
    }

    public void loadPicture (Uri pUri){
        //le toast envoit le popup qui nous donne le chemin (pUri)
        Toast.makeText(this,pUri.toString(),Toast.LENGTH_LONG).show();

        try  {
            InputStream is = getContentResolver().openInputStream(pUri);

            Bitmap img = BitmapFactory.decodeStream(is);
            ImageView grid = (ImageView) findViewById(R.id.imageView);
            ImageView iv = (ImageView)findViewById(R.id.imageView);
            iv.setImageBitmap(img);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Une fois la photo prise
   /* protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK)
        {
            // on redimentionne l'image et on l'affiche dans imageView
            Bitmap bmp = setPic();
            imageView.setImageBitmap(bmp);
        }
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode== Activity.RESULT_OK){
            if(requestCode== REQUEST_TAKE_PHOTO){
                // on redimentionne l'image et on l'affiche dans imageView
                Bitmap bmp = setPic();
                bitmapImage = bmp;
                Log.d("message", "bitmap exists .......................... " + bitmapImage);


                imageView.setImageBitmap(bmp);
            }else if(requestCode==SELECT_FILE){
                Uri imageUri = data.getData();
                setUriString(imageUri);
                //  imageUri.parse("content://media/external/images/media/33");
                // mCurrentPhotoPath = imageUri.getPath();

                Log.d("Message","Je suis dans on activity result" + imageUri.getPath());
                Log.d("Message","PAAATTTTHHHHHHHHH" + imageUri);


                try{
                    Bitmap bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                    bitmapImage = bmp;

                    Log.d("message", "bitmap exists .......................... " + bitmapImage);


                    // Bitmap bitmap = BitmapFactory.decodeFile(imageUri.toString());

                    // Bitmap bitmap = BitmapFactory.decodeFile(imageUri.toString());
                    imageView.setImageBitmap(bmp);

                }catch (IOException e){

                }

              /* Bitmap bmp = setPic();
               imageView.setImageBitmap(bmp);*/

            }
        }
    }


    // Création de l'image
    public File createImageFile() throws IOException {
        // Création d'un nom pour l'image grâce à la date courante
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // enregistrement du chemin absolu de l'image
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private Bitmap setPic() {
        // on récupère la taille de l'écran
        Display d = getWindowManager().getDefaultDisplay();
        DisplayMetrics m = new DisplayMetrics();
        d.getMetrics(m);

        // on créé des variables de tailles, celle que doit faire l'image une fois redimentionnée
        int targetW = m.widthPixels;
        int targetH = m.heightPixels;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        return BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
    }


    /*private Bitmap setPicGallery() {
        // on récupère la taille de l'écran
        Display d = getWindowManager().getDefaultDisplay();
        DisplayMetrics m = new DisplayMetrics();
        d.getMetrics(m);

        // on créé des variables de tailles, celle que doit faire l'image une fois redimentionnée
        int targetW = m.widthPixels;
        int targetH = m.heightPixels;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Uri imageUri = data.getData();
        try{
            Bitmap bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            imageView.setImageBitmap(bmp);

        } catch (IOException e){

        }

        return BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
    }*/

    // Enregistrement de l'image en local
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

        Log.d("Message","je suis dans addPic :)" + mCurrentPhotoPath);
        // mCurrentPhotoPath = "content://media" + mCurrentPhotoPath;
        //  Log.d("Message","après modification :)" + mCurrentPhotoPath);

        File f = new File(mCurrentPhotoPath);
        //Uri contentUri = Uri.fromFile(f);
        Uri contentUri = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".provider",f);
        Log.d("Message","******************" +contentUri.getPath());

        /* Uri contentUri = (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) ?
                FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".provider", f) :
                Uri.fromFile(f);*/
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    // Au clique sur une checkbox
    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        // on test quel checkbox a été cliqué puis si elle a aussi été coché on attribut une valeur a notre variable de niveau de  jeu
        switch(view.getId()) {
            case R.id.checkBox:
                if (checked) {
                    level = 2;
                }
                break;

            case R.id.checkBox2:
                if (checked) {
                    level = 3;

                }
                break;
            case R.id.checkBox3:
                if (checked) {
                    level = 4;

                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setUriString(Uri uriString) {
        this.uriString = uriString;
    }
    public void setChoice(int choice){
        this.choice= choice;
    }
    public int getChoice(){
        return this.choice;
    }
}
