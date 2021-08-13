package online.rkmhikai.ui.MyQuiz;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import online.rkmhikai.R;




public class CorrectDialog {
    private Context mContext;
    public Dialog correctDialog;
    private QuizActivity mquizActivity;

    public CorrectDialog(Context mContext) {
        this.mContext = mContext;
    }

    public void correctDialog(int score, QuizActivity quizActivity) {
        mquizActivity = quizActivity;
        correctDialog = new Dialog(mContext);
        correctDialog.setContentView(R.layout.correct_dialog);
        final Button btcorrectDialog = (Button) correctDialog.findViewById(R.id.bt_Score_Dialog);
        Score(score);  // Calling Method

        btcorrectDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                correctDialog.dismiss();
                mquizActivity.setQuestionView();
            }
        });
        correctDialog.show();
        correctDialog.setCancelable(false);
        correctDialog.setCanceledOnTouchOutside(false);
    }

    private void Score(int score) {
        TextView textScore = (TextView) correctDialog.findViewById(R.id.textView_Score);
        textScore.setText("Score: " + String.valueOf(score));
    }
}


