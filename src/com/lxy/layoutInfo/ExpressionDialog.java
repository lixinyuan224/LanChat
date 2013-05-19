package com.lxy.layoutInfo;

import com.lxy.LanChat.R;
import com.lxy.util.ExpressionUtil;
import com.lxy.util.MycommandNum;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class ExpressionDialog {
	private Context ctx;
	private EditText et;
	private Dialog alertDialog ;
	public ExpressionDialog(Context ctx,EditText et){
		this.ctx=ctx;
		this.et=et;
		init();
		
	}
	
	private void init(){
		LayoutInflater inflater=(LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vi=inflater.inflate(R.layout.alert_my_expression, null);
        GridView expression_grid=(GridView) vi.findViewById(R.id.expression_grid);
        final ExpressionAdapter expadp=new ExpressionAdapter(ctx);
        expression_grid.setAdapter(expadp);
        expression_grid.setOnItemClickListener(new OnItemClickListener() {//对表情选择对话框点击处理
			public void onItemClick(AdapterView<?> parent, View v, 
					int position, long id) {
				int i=expadp.getImageIds(position);//得到选中表情id
				//Toast.makeText(MyBalloonActivity.this, Integer.toString(i), Toast.LENGTH_LONG).show();
				Drawable drawable = ctx.getResources().getDrawable(i);
				drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
				//需要处理的文本，aa[2130837617]{10}aa是需要被替代的文本
				String text=et.getText().toString();//得到发送框里的内容
				String ss=Integer.toString(i);//把图片的ID转换为字符串
				String pngs="aa"+ss+"aa";
				SpannableString spannableString = null;
				try { 
					
		            spannableString = ExpressionUtil.getExpressionString(
		            		ctx,text+pngs, MycommandNum.zhengze); 
		          
		        } catch (NumberFormatException e) { 
		            e.printStackTrace(); 
		        } catch (SecurityException e) { 
		            e.printStackTrace(); 
		        } catch (IllegalArgumentException e) { 
		            e.printStackTrace(); 
		        } 
		        et.setText(spannableString);
			}
		});
        //创建表情选择对话框
         alertDialog = new AlertDialog.Builder(ctx).
        	    setTitle("请选择表情").
        	    setView(vi).
        	    setNegativeButton("取消", new DialogInterface.OnClickListener() {
        	     public void onClick(DialogInterface dialog, int which) {
        	     }
        	    }).
        	    create();
       
	}
	
	public void show(){
		alertDialog.show();
	}
	
	
	

}
