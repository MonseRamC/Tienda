package company.project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import company.project.ui.ver_productos.VerProductosViewModel;


public class SensorCamaraFragment extends Fragment {


    private FirebaseAuth mAuth;


    private static final int pic_id = 123;
    Button camera_open_id;
    Button save_camera_id;
    ImageView click_image_id;

    Bitmap photo;
    boolean photoTook = false;

    private VerProductosViewModel sensorCamaraViewModel;

    StorageReference storageRef;
    FirebaseStorage storage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_sensor_camara, container, false);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();


        camera_open_id = root.findViewById(R.id.camera_button);
        click_image_id = root.findViewById(R.id.click_image);
        click_image_id.setBackgroundColor(getResources().getColor(R.color.gray));

        save_camera_id = root.findViewById(R.id.camera_save);


        camera_open_id.setOnClickListener(v -> {
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera_intent, pic_id);
        });

        save_camera_id.setOnClickListener(v -> {
            if(photoTook){
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                mAuth = FirebaseAuth.getInstance();
                String photoName = mAuth.getUid();
                StorageReference photoRef = storageRef.child(photoName + ".jpg");
                StorageReference photoImagesRef = storageRef.child("images/"+ photoName +".jpg");

                UploadTask uploadTask = photoImagesRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(root.getContext(),exception.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(root.getContext(),"Foto actualizada",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else{
                Toast.makeText(root.getContext(),"Debe elegir una foto primero",Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id) {
            photo = (Bitmap) data.getExtras().get("data");

            click_image_id.setBackgroundColor(0);
            click_image_id.setImageBitmap(photo);
            photoTook = true;
        }
    }


}