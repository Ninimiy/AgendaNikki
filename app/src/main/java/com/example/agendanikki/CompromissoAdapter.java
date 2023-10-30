package com.example.agendanikki;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CompromissoAdapter extends RecyclerView.Adapter<CompromissoAdapter.ViewHolder> {

    private List<Compromisso> compromissos;
    private Context context;
    private OnDeleteClickListener onDeleteClickListener;

    public CompromissoAdapter(Context context, List<Compromisso> compromissos) {
        this.context = context;
        this.compromissos = compromissos;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        onDeleteClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_compromisso, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Compromisso compromisso = compromissos.get(position);
        holder.descricaoTextView.setText(compromisso.getDescricao());
        holder.dataTextView.setText(compromisso.getData());
        holder.horaTextView.setText(compromisso.getHora());

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return compromissos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView descricaoTextView;
        TextView dataTextView;
        TextView horaTextView;
        Button btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            descricaoTextView = itemView.findViewById(R.id.descricaoTextView);
            dataTextView = itemView.findViewById(R.id.dataTextView);
            horaTextView = itemView.findViewById(R.id.horaTextView);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }
}
