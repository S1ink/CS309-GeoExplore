package test.connect.geoexploreapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import test.connect.geoexploreapp.model.FeedItem;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {
    private List<FeedItem> items;

    public FeedAdapter(List<FeedItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent, false);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        FeedItem item = items.get(position);
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<FeedItem> allItems) {
        items=allItems;
    }

    public List<FeedItem> getItems() {
        return items;
    }

    static class FeedViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, department, type;

        FeedViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.itemTitle);
            description = itemView.findViewById(R.id.itemDescription);
            department = itemView.findViewById(R.id.itemDepartment);
            type = itemView.findViewById(R.id.itemType);
        }
    }
}
