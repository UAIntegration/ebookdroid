package org.ebookdroid.core.views;

import org.ebookdroid.R;
import org.ebookdroid.core.IBrowserActivity;
import org.ebookdroid.core.presentation.BooksAdapter;
import org.ebookdroid.core.views.Bookshelves.OnShelfSwitchListener;

import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BookcaseView extends LinearLayout {

    private final TextView shelfCaption;
    private final BooksAdapter adapter;

    private Bookshelves shelves;
    private IBrowserActivity base;

    public BookcaseView(IBrowserActivity base, BooksAdapter adapter) {
        super(base.getContext());

        this.adapter = adapter;
        this.base = base;

        setOrientation(VERTICAL);

        LinearLayout ll = (LinearLayout) LayoutInflater.from(base.getContext()).inflate(R.layout.bookshelf_caption,
                null);
        addView(ll);

        shelfCaption = (TextView) ll.findViewById(R.id.ShelfCaption);
        shelves = new Bookshelves(getContext());
        shelves.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));
        addView(shelves);

        adapter.registerDataSetObserver(new DataSetObserver() {

            @Override
            public void onChanged() {
                super.onChanged();
                BookcaseView.this.recreateViews();
                shelfCaption.setText(BookcaseView.this.adapter.getListName(shelves.getCurrentShelf()));
            }
        });

        shelves.setOnShelfSwitchListener(new OnShelfSwitchListener() {

            @Override
            public void onScreenSwitched(int screen) {
                shelfCaption.setText(BookcaseView.this.adapter.getListName(shelves.getCurrentShelf()));
            }
        });
        recreateViews();
    }

    protected synchronized void recreateViews() {
        shelves.updateShelves(base, adapter);
    }

    public void setCurrentList(Integer item) {
        shelves.setCurrentShelf(item);
        shelfCaption.setText(BookcaseView.this.adapter.getListName(shelves.getCurrentShelf()));
    }

    public void prevList() {
        int shelf = shelves.getCurrentShelf() - 1;
        if (shelf < 0) {
            shelf = 0;
        }
        shelves.setCurrentShelf(shelf);
        shelfCaption.setText(BookcaseView.this.adapter.getListName(shelves.getCurrentShelf()));
    }

    public void nextList() {
        int shelf = shelves.getCurrentShelf() + 1;
        if (shelf > adapter.getListCount() - 1) {
            shelf = adapter.getListCount() - 1;
        }
        shelves.setCurrentShelf(shelf);
        shelfCaption.setText(BookcaseView.this.adapter.getListName(shelves.getCurrentShelf()));
    }
}
