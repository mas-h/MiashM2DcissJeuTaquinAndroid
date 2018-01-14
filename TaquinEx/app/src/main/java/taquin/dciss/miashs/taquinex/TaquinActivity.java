package taquin.dciss.miashs.taquinex;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;

public class TaquinActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "debuggg";
    static final int REQUEST_TAKE_PHOTO = 1,SELECT_FILE=0;
    private TaquinAdapter taquinAdapter;
    private Button btnRejouer;
    private Animation animation;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taquin); // chargement du layout
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // Bloque l'application en mode portrait
        final GridView gridview = findViewById(R.id.gridview);

        // taille de l'écran du téléphone
        Display d = getWindowManager().getDefaultDisplay();
        DisplayMetrics m = new DisplayMetrics();
        d.getMetrics(m);


        int choiceTaq = getIntent().getIntExtra("choice",2); // on récupère le choix : soit camera soit gallery
        String imageUri = getIntent().getStringExtra("mCurrentPhotoPath"); // Récupère le lien de la photo envoyé en paramètre de Intent
        Uri galleryUri = Uri.parse(imageUri);
        Log.d("Message","!!!!!!!!!!!!!!!!!!!!!!!!!!" + imageUri);

        if(choiceTaq == REQUEST_TAKE_PHOTO ) {
            File image = new File(imageUri); // création d'une image à partir de ce lien

            Log.d("Message", "==============" + image.getAbsolutePath());

            if (!image.exists()) {

                Log.d("Message", "l'image n'existe pas ooooooooooooooooooo");

            }


            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions); // création d'une bitmap à partir du fichier image

            Log.d("Message", "++++++++++++++" + bitmap);
        }
        if (choiceTaq == SELECT_FILE) {
            try {


                InputStream is = getContentResolver().openInputStream(galleryUri);

                bitmap = BitmapFactory.decodeStream(is);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int level = getIntent().getIntExtra("level", 3); // récupère le niveau de la grille envoyé en paramètre de l'Intent
        gridview.setNumColumns(level); // Découpe en colonnes du gridView en fonction du niveau reçu

        // boutons restart et quitter
        btnRejouer = (Button) findViewById(R.id.rejouer);
        btnRejouer.setVisibility(View.GONE); // on cache le bouton quand le jeu est en cours
        // au clique sur le bouton on revient sur le premier écran de jeu (accueil)
        btnRejouer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaquinActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Envoi le niveau, la bitmap, et la taille de l'écran à l'adapteur pour traitement
        taquinAdapter = new TaquinAdapter(this, level, bitmap, m.widthPixels, m.heightPixels);
        gridview.setAdapter(taquinAdapter);

        CharSequence text = "Ne me décevez pas et remportez la victoire jeune padawan ! ";
        Toast toast = Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG);
        toast.show();

        // Au clique sur une case on effectue différents tests et traitements
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // Permutation des pièces de l'image & stockage de l'animation créée
                animation = taquinAdapter.permutation(position);
                View view = gridview.getChildAt(position); // on récupère la vue de l'enfant de la gridview à la position "position"
                animation.setDuration(300); // time de l'animation
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // Mise à jour de la grille pour voir la permutation après que l'animation soit terminée
                        gridview.invalidateViews();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                view.startAnimation(animation); // démarrage de l'animation

                if(taquinAdapter.bonOrdre()){
                    // Affichage d'un message pour féliciter le joueur
                    Toast toast = Toast.makeText(TaquinActivity.this, "Gagné ! Le jeu est terminé", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    gridview.setOnItemClickListener(null); // la partie est terminée, on stop le listner pour ne plus donner la possibilité de déplacer les cases
                    btnRejouer.setVisibility(View.VISIBLE); // le bouton pour revenir à l'accueil s'affiche
                }
            }
        });
    }
}
