package astrology.in;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class customAdapter extends ArrayAdapter {

    private static final int REQUEST_CALL = 1;
    private final String[] Title;
    private final String[] Discription;
    private final Context context;

    public customAdapter(Context applicationContext, String[] title, String[] discription) {
        super(applicationContext,R.layout.row,R.id.title,title);
        this.context = applicationContext;
        this.Title = title;
        this.Discription = discription;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = LayoutInflater.from(getContext()).inflate(R.layout.row, null, false);
        TextView title,discription;
        title = row.findViewById(R.id.title);
        discription = row.findViewById(R.id.discription);
        title.setText(this.Title[position]);
        discription.setText(this.Discription[position]);
        return row;
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }


}
