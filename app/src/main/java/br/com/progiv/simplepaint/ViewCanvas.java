package br.com.progiv.simplepaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class ViewCanvas extends View {

    private Path path;
    private Bitmap bitmap;
    private Canvas canvas;
    private float eixoX, eixoY;
    private int TOLERANCIA_MOVIMENTO = 5;
    private Linha linha;

    public ViewCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inicializaObjetos();
    }

    public void inicializaObjetosVerde(){
        path = new Path();
        Paint paint = Estilo.getEstiloParaLinhaVerde();
        linha = new Linha(getContext(), path, paint);
    }

    public void inicializaObjetosAmarelo(){
        path = new Path();
        Paint paint = Estilo.getEstiloParaLinhaAmarela();
        linha = new Linha(getContext(), path, paint);
    }

    //inicializar objetos
    private void inicializaObjetos(){
        path = new Path();
        linha = new Linha(getContext(), path);
    }

    //controlar inicio do toque
    private void inicioToque(float x, float y){
        path.moveTo(x, y);
        eixoX = x;
        eixoY = y;
    }

    //controlar movimento
    private void moveuDedoTela(float x, float y){
        float distanciaX = Math.abs(x - eixoY);
        float distanciaY = Math.abs(y - eixoY);

        if(distanciaX >= TOLERANCIA_MOVIMENTO ||
                distanciaY >= TOLERANCIA_MOVIMENTO){
            path.quadTo(eixoX, eixoY, (x+ eixoX)/2, (y+eixoY)/2 );
        }
        eixoX = x;
        eixoY = y;
    }

    //controlar final do toque
    private void tirouDedoTela(){
        path.lineTo(eixoX, eixoY);
    }

    //controlar limpeza da tela
    public void limparCanvas(){
        path.reset();
        inicializaObjetos();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        linha.desenharLinha(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                inicioToque(x, y);
                invalidate();
            break;

            case MotionEvent.ACTION_MOVE:
                moveuDedoTela(x, y);
                invalidate();
            break;

            case MotionEvent.ACTION_UP:
                tirouDedoTela();
                invalidate();
            break;
        }
        return true;
    }
}
