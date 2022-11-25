package company.project.ui.ver_productos;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import company.project.R;

public class VerProductosFragment extends Fragment {

    private VerProductosViewModel slideshowViewModel;

    private ListView listview;
    private ArrayList<String> productosNames;
    Context context;


    DatabaseReference reference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(VerProductosViewModel.class);
        View root = inflater.inflate(R.layout.fragment_ver_productos, container, false);
        context = getContext();

        listview = (ListView) root.findViewById(R.id.listNames);
        productosNames = new ArrayList<String>();

        FirebaseDatabase.getInstance()
                .getReference()
                .child("Productos")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot dataSnap : dataSnapshot.getChildren() ) {
                            System.out.println(dataSnap.getKey());
                            productosNames.add(dataSnap.getKey());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, productosNames);
                        listview.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                SharedPreferences sharedPreferences = PreferenceManager
                        .getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("SelectedProductos", productosNames.get(position));
                editor.apply();
                Navigation.findNavController(view).navigate(R.id.lectorQRFragment);
            }
        });


        return root;
    }



}
