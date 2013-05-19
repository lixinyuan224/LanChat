package com.lxy.LanChat;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import com.lxy.beans.ChatEntity;
import com.lxy.beans.ChatEntityAu;
import com.lxy.beans.UserInfo;
import com.lxy.layoutInfo.ChatAdapter;
import com.lxy.layoutInfo.ExpandableAdapter;
import com.lxy.layoutInfo.ExpressionDialog;
import com.lxy.layoutInfo.File_list_Thread;
import com.lxy.layoutInfo.ImageAdapter;
import com.lxy.layoutInfo.MyCartoon;
import com.lxy.layoutInfo.PopCard;
import com.lxy.managers.BeanManager;
import com.lxy.managers.LayoutManager;
import com.lxy.managers.MyThreadManager;
import com.lxy.managers.ProtocolManager;
import com.lxy.net.MyNetSendMessage;
import com.lxy.net.MyOnlineOfflineTh;
import com.lxy.net.MynetListener;
import com.lxy.net.voice.*;
import com.lxy.net.file.FileControl;
import com.lxy.net.file.FileTransAlertDialog;
import com.lxy.util.CurrentTime;
import com.lxy.util.FindMyinfo;
import com.lxy.util.MyProtocol;
import com.lxy.util.MycommandNum;
import com.lxy.util.SensitiveWordUtils;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.ExpandableListView.OnChildClickListener;
/**
 * 
 * @author lixy
 *
 */
public class MainActivity extends Activity {
	private LayoutManager layoutManager; //布局窗口数据管理
	private BeanManager   beanManager;   //全局数据管理
	private FindMyinfo findmyinfo;       //个人信息文件存取
	private ProtocolManager protocolManager; //协议处理
	private SensitiveWordUtils sensitive;    //敏感字过滤
	private MyThreadManager myThreadManager;//线程处理
	private FileControl fc;                 //文件传输管理
	private VoiceAlertDialog voiceAlertDia;  // 语音对话框
	private FileTransAlertDialog fileALertDia;  // 文件传输对话框
	
	private MynetListener mynetListener;  //数据包接收监听线程

	public  MyHandler hd=new MyHandler();  //消息接收handler
	
	private boolean isInit=false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        findmyinfo=new FindMyinfo(this);
        init();
        if(findmyinfo.findUser()){
        	isInit=true;
        	
	        if(isWifiOn()&&isInit){
	        	mynetListener=new MynetListener(hd, beanManager);
	        	mynetListener.start();
	        	Log.i("mainactivity--mynetListener","mynetListener.start();");
	        	myThreadManager.getMyOnlineOfflineTh().noticeOnline();
	        	myThreadManager.getMyOnlineOfflineTh().start();
	        }else{
	        	Toast.makeText(this, "wifi 没有打开", Toast.LENGTH_SHORT).show();
	        }
        }
        _welcome();
       
        
    }
    
    public void init(){//初始化
		sensitive=new SensitiveWordUtils(MainActivity.this);
    	layoutManager=new LayoutManager();
        beanManager=new BeanManager();
        if(findmyinfo.findUser()){
        	beanManager.setUserinfo(findmyinfo.getUserinfo());
        	layoutManager.getInfosetting().setMyInfo(findmyinfo.getUserinfo());
        	Log.i("init-->", Integer.toString(beanManager.getMyInfo().getHeadImgId()));
        }
    	myThreadManager=new MyThreadManager(beanManager);
    	fc=new FileControl(hd);
        protocolManager=new ProtocolManager(beanManager,layoutManager,hd,this,fc);
    }
    
    public void _welcome(){
 	   MyCartoon cartoon=new MyCartoon(this,hd);
       setContentView(cartoon);
    }
    
    public void startLayout(){
    	if(isInit){
    		
    		_myExpandble();
    	}
    	else{
    		_myPersonalInfo();
    	}
    }
    
    
    public void _myPersonalInfo(){//个人信息界面
    	
    	final EditText et_nickname; //昵称
    	final EditText et_sex;      //性别
    	final EditText et_signature;//签名
    	final EditText et_truename; //真名
    	final int headId=-1;        //头像的id
    	ImageView imgview;          // 头像
    	Button confirm;             //确定按钮
    	Button cancle;              //取消按钮
    	Button setUp;               //设置头像按钮
    	
    	setContentView(R.layout.layout_myinfo);
    	
		et_nickname = (EditText) findViewById(R.id.et_name);
		et_sex = (EditText) findViewById(R.id.et_sex);
		et_truename=(EditText) findViewById(R.id.et_no1);
		et_signature = (EditText) findViewById(R.id.et_signature);
		imgview=(ImageView) findViewById(R.id.img_myPhoto);
		confirm = (Button) findViewById(R.id.bu_confirm);
		cancle=(Button) findViewById(R.id.bu_cancle);
		setUp=(Button) findViewById(R.id.bu_findPhoto);
	
		if(layoutManager.getInfosetting().getId()==null){
			layoutManager.getInfosetting().setId(findmyinfo.myId());
		}
		if(layoutManager.getInfosetting().getIp()==null){
			layoutManager.getInfosetting().setIp(findmyinfo.myIp());
		}
		Log.i("_myPersonalInfo","if(layoutManager.getInfosetting().getIp()==null");
		if(beanManager.getMyInfo()!=null&&!beanManager.getMyInfo().getnickname().equals("admin")){
			Log.i("_myPersonalInfo","if(beanManager.getMyInfo()!=null&&!bea");
			et_nickname.setText(beanManager.getMyInfo().getnickname());
			et_sex.setText(beanManager.getMyInfo().getSex());
			et_truename.setText(beanManager.getMyInfo().getTruename());
			et_signature.setText(beanManager.getMyInfo().getSignature());
			imgview.setImageResource(beanManager.getMyInfo().getHeadImgId());
		}else if(layoutManager.getInfosetting().isNull()){
			et_nickname.setText(layoutManager.getInfosetting().getNickname());
			et_sex.setText(layoutManager.getInfosetting().getSex());
			et_truename.setText(layoutManager.getInfosetting().getTruename());
			et_signature.setText(layoutManager.getInfosetting().getSignature());
			imgview.setImageResource(Integer.parseInt(layoutManager.getInfosetting().getHeadIcon()));
		}else{
			imgview.setImageResource(headId);
		}
		
		setUp.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				String nickname=et_nickname.getText().toString();
				String truename=et_truename.getText().toString();
				String sex=et_sex.getText().toString() ;
				String signature=et_signature.getText().toString();
				layoutManager.getInfosetting().setNickname(nickname);
				layoutManager.getInfosetting().setSex(sex);
				layoutManager.getInfosetting().setSignature(signature);
				layoutManager.getInfosetting().setTruename(truename);
				
				_myExpression();
			}
		});	
		confirm.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				String nickname=et_nickname.getText().toString();
				String truename=et_truename.getText().toString();
				String sex=et_sex.getText().toString() ;
				String signature=et_signature.getText().toString();
				
				if (!truename.equals("")&&!nickname.equals("")&&!sex.equals("")&&!signature.equals("")){
					
					layoutManager.getInfosetting().setNickname(nickname);
					layoutManager.getInfosetting().setSex(sex);
					layoutManager.getInfosetting().setSignature(signature);
					layoutManager.getInfosetting().setTruename(truename);
//					layoutManager.getInfosetting().setHeadIcon(headIcon)
				//	findmyinfo.creatUser(nickname,truename,sex,signature,headId);
//					et_nickname.setText(null);
//					et_sex.setText(null);
//					et_signature.setText(null);
//					et_truename.setText(null);

					UserInfo u=layoutManager.getInfosetting().getMyinfo();
					findmyinfo.creatUser(u);
					
					Log.i("_myPersonalInfo( confirm.Headid",Integer.toString(u.getHeadImgId()));
					if (findmyinfo.findUser()) {
						beanManager.setUserinfo(findmyinfo.getUserinfo());
						isInit=true;
						startLayout();
					}
				}else{
					Toast.makeText(MainActivity.this, "请输入信息", Toast.LENGTH_SHORT).show();
				}
			}
		});
		cancle.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if (!isInit) 
				     Toast.makeText(MainActivity.this, "填写信息", Toast.LENGTH_LONG).show();
				else 
					_myExpandble();
			}
		});
		
    }

    public void _myExpression(){//头像选择界面
    	final ImageAdapter imageadapter;
    	setContentView(R.layout.layout_expression);
		ImageView backMyinfo=(ImageView) findViewById(R.id.return_myinfo);
		imageadapter=new ImageAdapter(this);
		GridView gridview = (GridView) findViewById(R.id.gridview);//取得GridView对象
		gridview.setAdapter(imageadapter);	//添加元素给gridview
//		gridview.setBackgroundResource(R.drawable.bg0);// 设置Gallery的背景
		
		gridview.setOnItemClickListener(new OnItemClickListener() {		//事件监听
			public void onItemClick(AdapterView<?> parent, View v, int position, long id)
			{
				int i=imageadapter.getImageIds(position);
				Log.i(" onItemClick(AdapterView<?>",Integer.toString(i));
				layoutManager.getInfosetting().setHeadIcon(Integer.toString(i));
				beanManager.getMyInfo().setHeadImgId(i);
			//	findmyinfo.getUserinfo().setHeadImgId(i);
				_myPersonalInfo();
			}
		});
		backMyinfo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				_myPersonalInfo();
			}
		});
    }
    
    public void _myExpandble(){//主界面 朋友列表
    	 TextView btnModule1 ;
    	 TextView btnModule2;
    	 ImageView myPhoto;
         Button refresh=null;
         
         setContentView(R.layout.layout_expandable);
         
         layoutManager.getExpandablesetting().setExpandableListView_one(
        		 (ExpandableListView)findViewById(R.id.expandableListView));//多级列表
         myPhoto=(ImageView) findViewById(R.id.MyPhoto);
         myPhoto.setImageResource(beanManager.getMyInfo().getHeadImgId());//设置头像
         refresh=(Button) findViewById(R.id.refresh);
         layoutManager.getExpandablesetting().setTextview((TextView) findViewById(R.id.cust_title));
         layoutManager.getExpandablesetting().getTextview().setText(beanManager.getMyInfo().getnickname());//设置姓名
         Log.i("layoutManager.getExpandablesetting().getTextview().",layoutManager.getExpandablesetting().getTextview().getTextColors().toString());
         layoutManager.getExpandablesetting().getTextview().
         	setOnClickListener(new OnClickListener() {//状态按钮
   			public void onClick(View arg0) {
   				final String[] state = new String[] { "在线", "隐身"}; 
   		        Dialog alertDialog = new AlertDialog.Builder(MainActivity.this). 
   		                setTitle("请选择状态"). 
   		                setItems(state, new DialogInterface.OnClickListener() { 
   		                    public void onClick(DialogInterface dialog, int which) { 
   		                    	Log.i("state",Integer.toString(which));
   		                    	switch(which){
   		                    	case 0:{
   		                    		layoutManager.getExpandablesetting().getTextview().setTextColor(Color.WHITE);
   		                    		MyOnlineOfflineTh th=new MyOnlineOfflineTh(beanManager.getMyInfo());
   		                    		th.noticeOnline();
   		                    		th.start();
   		                    		beanManager.setInvisibility(true);
   		                    	}break;
   		                    	case 1:{
   		                    		layoutManager.getExpandablesetting().getTextview().setTextColor(Color.BLACK);
   		                    		MyOnlineOfflineTh th=new MyOnlineOfflineTh(beanManager.getMyInfo());
   		                    		th.noticeOffline();
   		                    		th.start();
   		                    		beanManager.setInvisibility(false);
   		                    	}break;
   		                    	}
   		                    }
   		                }). 
   		                setNegativeButton("取消", new DialogInterface.OnClickListener() {
   		                    public void onClick(DialogInterface dialog, int which) { 
   		                    	
   		                    } 
   		                }). 
   		                create(); 
   		        alertDialog.show(); 
   			}
   		});
         btnModule1 = (TextView) findViewById(R.id.btnModule1);
         btnModule2 = (TextView) findViewById(R.id.btnModule2);
         btnModule1.setBackgroundResource(R.color.green);
         btnModule2.setBackgroundResource(R.color.bottom);
         
         btnModule2.setOnClickListener(new OnClickListener() {//空间按钮
             public void onClick(View v) {
                _myZone();
                 
             }
         });
         refresh.setOnClickListener(new OnClickListener() {//刷新按鈕   55555
 			public void onClick(View v) {
// 				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
// 					Toast.makeText(MainActivity.this, "外部存储可写", Toast.LENGTH_LONG).show();
	 			beanManager.refresh();
	 			MyOnlineOfflineTh myonoffline=new MyOnlineOfflineTh(beanManager.getMyInfo());
	 			myonoffline.noticeOnline();
	 			myonoffline.start();
// 				myThreadManager.getMyOnlineOfflineTh().noticeOnline();
// 				if(myThreadManager.getMyOnlineOfflineTh().isAlive()){
// 					Log.i("refresh","myThreadManager.getMyOnlineOfflineTh().isAlive()");
// 					myThreadManager.getMyOnlineOfflineTh().interrupt();
// 					myThreadManager.getMyOnlineOfflineTh().start();
// 					Log.i("refresh","myThreadManager.getMyOnlineOfflineTh().isAlive()");
// 				}else{
// 					myThreadManager.getMyOnlineOfflineTh().start();
// 					Log.i("refresh","myThreadManager.getMyOnlineOfflineTh().dead");
// 				}
//	 	
	 			_myExpandble();
 			}
 		});
         layoutManager.getExpandablesetting().setExlistadp(
        		 new ExpandableAdapter(MainActivity.this,beanManager,layoutManager));
         layoutManager.getExpandablesetting().getExpandableListView_one()
         			  .setAdapter(layoutManager.getExpandablesetting().getExlistadp());
         this.registerForContextMenu(
        		 layoutManager.getExpandablesetting().getExpandableListView_one());//监听长按事件，一定要注册
         layoutManager.getExpandablesetting().getExpandableListView_one()
         	.setOnChildClickListener(new OnChildClickListener() {//点击子列表跳转
     		public boolean onChildClick(ExpandableListView parent, View v,
     				int groupPosition, int childPosition, long id) {
     					Log.i("onclick", groupPosition+"--"+childPosition);
     					
     				UserInfo friend=beanManager.getChildren().get(groupPosition).get(childPosition);
     				layoutManager.getMyballonsetting().setCurrPerson(friend);
     				layoutManager.getMyballonsetting().setChatList(beanManager.getMyUserChatMap().get(friend.getId()));
     				
     				boolean flag=true;
     				Iterator<UserInfo> ui=beanManager.getChildren().get(1).iterator();
     				while(ui.hasNext()){
     					UserInfo i=ui.next();
     					if(i.getIp().equals(friend.getIp())&&i.getnickname().equals(friend.getnickname())&&
     							i.getHeadImgId()==friend.getHeadImgId()){
     						flag=false;
     					}
     				}
     				if(flag){ //好友列表中没有此人，加入
     					beanManager.getChildren().get(1).add(friend);
     				}
     				
     				Iterator<UserInfo> ui1=beanManager.getChildren().get(3).iterator();
     				int i=0;
     				while(ui1.hasNext()){
     					if(ui1.next().getIp().equals(friend.getIp())){//如果消息列表中有，则删除
     						Log.i("ui1 -->","delete 3");
     						beanManager.getChildren().get(3).remove(i);
     					}
     					i+=1;
     				}
     			
     		 		_myBallonChat(friend);
     			 	
     			return false;
     			}
     		});
    }
    
    public void _myBallonChat(UserInfo fri){//聊天界面
    	final UserInfo friend;
    	if(fri==null)  
    		friend=layoutManager.getMyballonsetting().getCurrPerson();
    	else 
    		friend =fri;
    	
    	setContentView(R.layout.layout_balloon);
    	
    	 Button sendButton = null;//发送按钮
    	 final ImageView popButton;//弹出框
    	 ImageButton voice=null;//语音
    	 ImageButton expression;//表情选择
    	 ImageButton filechoose;//文件选择
    	 final EditText contentEditText;//聊天编辑框
    	 final TextView title;
    	 ListView listview;
    	 ImageView imgReturn;//返回按钮
    	 final LinearLayout choosebar;//选项框
    	 
    	
    	//注册控件
 		contentEditText = (EditText)findViewById(R.id.et_content);///编辑内容框
 		sendButton = (Button) findViewById(R.id.btn_send);
 		popButton=(ImageView) findViewById(R.id.btn_receive);
 		voice=(ImageButton) findViewById(R.id.voice );
 		imgReturn=(ImageView) findViewById(R.id.return_viewGroup);
 		title=(TextView) findViewById(R.id.title);
 		listview=(ListView)findViewById(R.id.listview);
 		choosebar=(LinearLayout) findViewById(R.id.choose_bar);
 		expression=(ImageButton) findViewById(R.id.expression);
 		filechoose=(ImageButton) findViewById(R.id.filechoose);
 		choosebar.setVisibility(View.GONE);//隐藏弹出框
 		
 		layoutManager.getMyballonsetting().setChatListView(listview);
 		title.setText("与"+friend.getnickname()+"聊天中");
 		title.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			
				PopCard pop=new PopCard(MainActivity.this, friend, title);
			}
		});
 		//ArrayList<ChatEntity> chatelist=beanManager.getMyUserChatMap().get(friend.getIp());
 		//设置监听setChatAdapter
 		if( beanManager.getMyUserChatMap().get(friend.getIp())==null){
 			Log.i("beanManager.getMyUserChatMap().get(friend.getIp())", "null");
 			beanManager.getMyUserChatMap().put(friend.getIp(), new ArrayList<ChatEntity>());
 		}
 		layoutManager.getMyballonsetting().setChatAdapter(
 				new ChatAdapter(this, beanManager.getMyUserChatMap().get(friend.getIp())));
 		listview.setAdapter(layoutManager.getMyballonsetting().getChatAdapter());
 
		sendButton.setOnClickListener(new OnClickListener() {//发送消息监听器
			public void onClick(View v) {
				if (!contentEditText.getText().toString().equals("")) {
					//发送消息
					//send();
					String s1=contentEditText.getText().toString();
					String s=sensitive.replace(s1);//过滤后的文字内容
					Log.i("sensitive.replace(s1)-->", sensitive.replace(s1)+"|"+s);
					Log.i("sendButton","message:"+s);
					ChatEntityAu chatEntity=new ChatEntityAu(
							beanManager.getMyInfo().getHeadImgId(), 
							s, CurrentTime.getTime(), true);
//					Log.i("sendButton","message:"+chatEntity.getSspanable());
					if( beanManager.getMyUserChatMap().get(friend.getIp())==null)
			 			Log.i("beanManager.getMyUserChatMap().get(friend.getIp())", "null");
					else
						Log.i("beanManager.getMyUserChatMap().get(friend.getIp())", "not null");
					layoutManager.getMyballonsetting().setCurrChatEntity(chatEntity);
					layoutManager.getMyballonsetting().getChatAdapter().notifyDataSetChanged();
					MyProtocol myProtoco=new MyProtocol();
					myProtoco.setChatEntity(chatEntity);
					myProtoco.setCmd(MycommandNum.Message);
					myProtoco.setReceiver(friend);
					Log.i("beanManager myinfo","--"+beanManager.getMyInfo().getIp());
					myProtoco.setSender(beanManager.getMyInfo()); // 发送
//					myProtoco.setTimeStamp(CurrentTime.getTim());
					MyNetSendMessage mns=new MyNetSendMessage();
					mns.setData(myProtoco);
					mns.start();
					ChatEntity chat=new ChatEntity(chatEntity);
					chat.toSpannable(MainActivity.this);
					beanManager.getMyUserChatMap().get(friend.getIp()).add(chat);//加入聊天记录队列
					//Log.i("beanManager.getMyUserChatMap().get(friend.getIp())",beanManager.getMyUserChatMap().get(friend.getIp()).get(0).getSspanable());
					layoutManager.getMyballonsetting().getChatAdapter().notifyDataSetChanged();
					Iterator<ChatEntity> it=beanManager.getMyUserChatMap().get(friend.getIp()).iterator();
					while(it.hasNext())
						Log.i("chatEntity", it.next().getSspanable());
					
				} else {
					Toast.makeText(MainActivity.this, "内容不能为空",
							Toast.LENGTH_SHORT).show();
				}
				contentEditText.setText("");
			}
		});//监听发送按钮
		popButton.setOnClickListener(new OnClickListener(){//监听弹出框
			public void onClick(View arg0) {
				//popChoosebar();//弹出隐藏的选项框
				if(!layoutManager.getMyballonsetting().isIspop()){
					choosebar.setVisibility(View.VISIBLE);
					layoutManager.getMyballonsetting().setIspop(true);
					popButton.setImageResource(R.drawable.png_1050_down);
				}
				else{
					choosebar.setVisibility(View.GONE);
					layoutManager.getMyballonsetting().setIspop(false);
					popButton.setImageResource(R.drawable.png_1050_up);
					}
			}
		});//监听接收按钮
		imgReturn.setOnClickListener(new OnClickListener(){//返回按钮监听
			public void onClick(View v) {
				layoutManager.getMyballonsetting().setCurrPerson(null);
				
				_myExpandble();
			}
			
		});//监听返回按钮
		
		filechoose.setOnClickListener(new OnClickListener(){//文件选择监听器
			public void onClick(View v) {
				_myFileChoose();
			}
		});//监听文件选择按钮
		
		expression.setOnClickListener(new  OnClickListener(){//表情选择监听事件
			public void onClick(View v) {
				ExpressionDialog ed=new ExpressionDialog(MainActivity.this, contentEditText);
				ed.show();
			}
		});//监听表情选择

		voice.setOnClickListener(new OnClickListener(){//监听语音
			public void onClick(View arg0) {
				MyProtocol myprotocolVoice=new MyProtocol();
				myprotocolVoice.setCmd(MycommandNum.voice);
				myprotocolVoice.setSender(beanManager.getMyInfo());
				myprotocolVoice.setReceiver(layoutManager.getMyballonsetting().getCurrPerson());
//				myprotocolVoice.setTimeStamp(CurrentTime.getTim());
				MyNetSendMessage mns=new MyNetSendMessage();
				mns.setData(myprotocolVoice);
				mns.start();
				voiceAlertDia=new VoiceAlertDialog(layoutManager.getMyballonsetting().getCurrPerson(), MainActivity.this,beanManager,hd);
				voiceAlertDia.creatDialog(true);
				voiceAlertDia.show();
			}
		});
	
    }
    
    public void _myZone(){
    	Toast.makeText(this, "未完成", Toast.LENGTH_SHORT).show();
    }
    
    public void _myFileChoose(){//文件选择界面
    	Log.i("_myFileChoose()","start");
    	 ListView list = null;
    	 ImageView imreturn;
    	 setContentView(R.layout.layout_filescanner);
         list = (ListView) findViewById(R.id.lsview);
         imreturn=(ImageView) findViewById(R.id.filescanner_return);
         layoutManager.getMyfilechoose().setList(list);
         layoutManager.getMyfilechoose().
			setAllFilenames(new ArrayList<Map<String, Object>>());
		 layoutManager.getMyfilechoose().
			setAllFileitems(new ArrayList<File>());
         layoutManager.getMyfilechoose().setFilelistth(new File_list_Thread(layoutManager, this));
 		 File filepath = new File(java.io.File.separator);
         imreturn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				_myBallonChat(null);
			}
		});
         layoutManager.getMyfilechoose().getFilelistth().execute(filepath);
         if(layoutManager.getMyfilechoose().getList()!=null){
        	 Log.i("getList()", ".getList()!=null");
         }else{
        	 Log.i("getList()", ".getList()=null");
         }
    
         layoutManager.getMyfilechoose().getList().
         	setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {

					Log.i("_myFileChoose()","setOnItemClickListener(");
					File currFile=(File) layoutManager.getMyfilechoose().
							getAllFileitems().get(arg2);//这里可能会奔溃
					
					if(currFile.isDirectory()){
						String []s=currFile.list();
						if(s!=null){
							if(s.length!=0){
								layoutManager.getMyfilechoose().
									setAllFilenames(new ArrayList<Map<String, Object>>());
								layoutManager.getMyfilechoose().
									setAllFileitems(new ArrayList<File>());
								layoutManager.getMyfilechoose().setFilelistth(
										new File_list_Thread(layoutManager,MainActivity.this));
								layoutManager.getMyfilechoose().
								getFilelistth().execute(currFile);
							}else{
								Toast.makeText(MainActivity.this, "空文件夹", 
										Toast.LENGTH_SHORT).show();
							}
						}else{
							Toast.makeText(MainActivity.this, "空文件夹", 
									Toast.LENGTH_SHORT).show();
						}
					}else{ //如果是文件，发送
						Toast.makeText(MainActivity.this, 
								currFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
						//传输文件开始
						File f=new File(currFile.getAbsolutePath());
						layoutManager.getMyballonsetting().
							setFilePath(f.getAbsolutePath());//发送文件路径
						MyProtocol myprotocolFiletrans=new MyProtocol();
						myprotocolFiletrans.setCmd(MycommandNum.FILE);
						myprotocolFiletrans.setSender(beanManager.getMyInfo());
						myprotocolFiletrans.setReceiver(layoutManager.getMyballonsetting().getCurrPerson());
						myprotocolFiletrans.setFileLength(Long.toString(f.length()));
						myprotocolFiletrans.setFilename(f.getName());
						myprotocolFiletrans.setFilePath(f.getAbsolutePath());
						MyNetSendMessage mns=new MyNetSendMessage();
						mns.setData(myprotocolFiletrans);
						mns.start();
						_myBallonChat(null);
						fileALertDia=new FileTransAlertDialog(myprotocolFiletrans, MainActivity.this,fc,beanManager);
						fileALertDia.creatDialog(true);
						fileALertDia.show();
					}
				}
			});
				
    
         
    }
    
 
	public boolean isWifiOn(){//判断wifi是否打开
    	ConnectivityManager mConnectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if(mConnectivity != null){
			NetworkInfo[] infos = mConnectivity.getAllNetworkInfo();
			
			if(infos != null){
				for(NetworkInfo ni: infos){
					if("WIFI".equals(ni.getTypeName()) && ni.isConnected())
						return true;
				}
			}
		}
		
		return false;
    }
  
	
    @Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
		
	     ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) menuInfo;
	     int type = ExpandableListView.getPackedPositionType(info.packedPosition);
	    
//			得到父位置和子位置
//		System.out.println(ExpandableListView.getPackedPositionGroup(info.packedPosition)+"->father");
//		System.out.println(ExpandableListView.getPackedPositionChild(info.packedPosition)+"->child");
			/*获得长按的类型，父型或子型*/

    	if(type==ExpandableListView.PACKED_POSITION_TYPE_GROUP){	
//    		长按父位置不处理
    	}else if(type==ExpandableListView.PACKED_POSITION_TYPE_CHILD){
    		if(ExpandableListView.getPackedPositionGroup(info.packedPosition)==1){
    			Toast.makeText(MainActivity.this, "正在通话分组不能移动", Toast.LENGTH_SHORT).show();
    		}else if(ExpandableListView.getPackedPositionGroup(info.packedPosition)==3){
    			
    		}else{
	    		layoutManager.getExpandablesetting()
	    			.setGroupPosition(ExpandableListView
	    							.getPackedPositionGroup(info.packedPosition));
	    		layoutManager.getExpandablesetting()
	    			.setChildPosition(ExpandableListView
	    							.getPackedPositionChild(info.packedPosition));
	    		layoutManager.getExpandablesetting()//得到要移动的信息
	    					.setFriendmove(beanManager.getChildren()
	    									.get(layoutManager.getExpandablesetting()
	    											.getGroupPosition())
	    									.get(layoutManager.getExpandablesetting()
	    											.getChildPosition()));
	    	
	    		
	    		layoutManager.getExpandablesetting().getExpandableListView_one()
	    			.collapseGroup(layoutManager.getExpandablesetting()
	    				.getGroupPosition());//收起当前子项所在组
	    		menu.setHeaderTitle("移动到：");
	        	for(int i=0;i<layoutManager.getExpandablesetting().getGroupArray().size();i++){
	        		if(i!=layoutManager.getExpandablesetting().getGroupPosition()&&i!=1&&i!=3)
	        			menu.add(1,i,2,layoutManager.getExpandablesetting().getGroupArray().get(i));
	        	}
    		}
    	}
    	super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {//移动后的处理
		switch (item.getItemId()) { 
        case 0:{
        	beanManager.getChildren()//从此组中删除此人
			.get(layoutManager.getExpandablesetting()
			.getGroupPosition())
			.remove(layoutManager.getExpandablesetting().getChildPosition());
        	
        	beanManager.getChildren().get(0).add(
        			layoutManager.getExpandablesetting().getFriendmove());
        	layoutManager.getExpandablesetting().setChildPosition(0);
        	layoutManager.getExpandablesetting().setGroupPosition(0);
        	layoutManager.getExpandablesetting().setFriendmove(null);
		}break;
       
        case 2:{
        	beanManager.getChildren()//从此组中删除此人
			.get(layoutManager.getExpandablesetting()
			.getGroupPosition())
			.remove(layoutManager.getExpandablesetting().getChildPosition());
        	
        	beanManager.getChildren().get(2).add(//添加到相应的组
        			layoutManager.getExpandablesetting().getFriendmove());
        	layoutManager.getExpandablesetting().setChildPosition(0);
        	layoutManager.getExpandablesetting().setGroupPosition(0);
        	layoutManager.getExpandablesetting().setFriendmove(null);
        }break;
//        case 3:
//        	beanManager.getChildren().get(3).add(
//        			layoutManager.getExpandablesetting().getFriendmove());
//        	layoutManager.getExpandablesetting().setChildPosition(0);
//        	layoutManager.getExpandablesetting().setGroupPosition(0);
//        	layoutManager.getExpandablesetting().setFriendmove(null);
//            break;
       }
       return false;
	}
    
	
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

     @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	 switch (item.getItemId()) { 
	 	    case R.id.menu_settings: { 
	 	    	_myPersonalInfo();
	 	    	
	 	    }break; 
	 	    case R.id.menu_about:{
	 	       LayoutInflater layoutInflater = LayoutInflater.from(this); 
	 	       View contentview = layoutInflater.inflate(R.layout.layout_popupwindow, null); 
	 	       Dialog alertDialog = new AlertDialog.Builder(this). 
	 	               setTitle("about"). 
	 	               setView(contentview). 
	 	               setNegativeButton("取消", new DialogInterface.OnClickListener() { 
	 	                   public void onClick(DialogInterface dialog, int which) { 
	 	                        // TODO Auto-generated method stub  
	 	                   } 
	 	               }). 
	 	               create(); 
	 	       alertDialog.show(); 
	 	    	
	 	    }break;
	 	    case R.id.menu_exit: {
	 		    finish();  
	 	    }break; 
	 	} 
		return super.onOptionsItemSelected(item);
	}

     @Override
 	public boolean onKeyDown(int keyCode, KeyEvent event) {
    	 if (keyCode == KeyEvent.KEYCODE_BACK )  
         {  
    		 Dialog alertDialog = new AlertDialog.Builder(this). 
//    	                setTitle(""). 
    	                setMessage("您确定退出吗？"). 
//    	                setIcon(R.drawable.ic_launcher). 
    	                setPositiveButton("确定", new DialogInterface.OnClickListener() {                     
    	                    public void onClick(DialogInterface dialog, int which) { 
    	                    	finish();    
    	                    } 
    	                }). 
    	                setNegativeButton("取消", new DialogInterface.OnClickListener() {                     
    	                    public void onClick(DialogInterface dialog, int which) { 
    	                    	
    	                    } 
    	                }). 
    	                create(); 
    	        alertDialog.show(); 
    	        return true;  
    	        
         } else {  
       
             return super.onKeyDown(keyCode, event);  
       
         }  
         
 	}

     
	@Override
 	protected void onDestroy() {
		if(fileALertDia!=null){
			fileALertDia.dissmiss();
		}
		if(voiceAlertDia!=null){
			voiceAlertDia.dissmiss();
		}
		if(isWifiOn()&&mynetListener!=null){
			mynetListener.disconnectSocket();
		}
    	
     	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
     	
     	super.onDestroy();
     		//android.os.Process.killProcess(android.os.Process.myPid());
 		
 	}


     
     
     
     class MyHandler extends Handler{
 		@Override
 		public void handleMessage(Message msg) {
 			protocolManager=new ProtocolManager(beanManager, layoutManager, hd, MainActivity.this, fc);
 			switch(msg.what){
 			case 1:{
 				startLayout();
 			}break;
 			case MycommandNum.offLinere:{
 				UserInfo user=(UserInfo)msg.obj;
 				Toast.makeText(MainActivity.this, user.getnickname()+"已下线!!", Toast.LENGTH_SHORT).show();
 				beanManager.refresh();
	 			MyOnlineOfflineTh myonoffline=new MyOnlineOfflineTh(beanManager.getMyInfo());
	 			myonoffline.noticeOnline();
	 			myonoffline.start();
 				_myExpandble();
 			}break;
 			case MycommandNum.receive:{
 				MyProtocol mypro=(MyProtocol) msg.obj;
 				protocolManager.setPreProtocol(mypro);
 				protocolManager.start();
 			}break;
 			case MycommandNum.Message:{
 				if(layoutManager.getMyballonsetting().getChatAdapter()!=null)
 				layoutManager.getMyballonsetting().getChatAdapter().notifyDataSetChanged();
 			}break;
 			case MycommandNum.onLinere:{
 				if(	layoutManager.getExpandablesetting().getExlistadp()!=null)
 				layoutManager.getExpandablesetting().getExlistadp().notifyDataSetChanged();
 			}break;
 			case MycommandNum.FILESUCCESS:{
 				fileALertDia.dissmiss();
 				Toast.makeText(MainActivity.this, "文件接收成功", Toast.LENGTH_SHORT).show();

 			}break;
 			case MycommandNum.FILEREORNOT:{
 				final MyProtocol pro=(MyProtocol) msg.obj;
 				fileALertDia=new FileTransAlertDialog(
 						pro, MainActivity.this,fc,beanManager);
 				fileALertDia.creatDialog(false);
 				fileALertDia.show();
 				
 			}break;
 			case MycommandNum.FILEYES:{
 				fileALertDia.changeLayout();
 			
 				
 			}break;
 			case MycommandNum.FILENO:{
 				fileALertDia.dissmiss();
 				Toast.makeText(MainActivity.this, "对方拒绝接收", Toast.LENGTH_SHORT).show();
 			}break;
 			case MycommandNum.FILEPLUS:{
 				int per=(Integer) msg.obj;
 				fileALertDia.plus(per);
 			}break;
 			case MycommandNum.FILEING:{
 				if(fileALertDia!=null){
 					fileALertDia.dissmiss();
 					Toast.makeText(MainActivity.this, "对方正在文件传输中！", Toast.LENGTH_SHORT).show();
 				}
 			}break;
 			case MycommandNum.VOICEREORNOT:{ 
 				final MyProtocol pro=(MyProtocol) msg.obj;
 				voiceAlertDia=new VoiceAlertDialog(pro.getSender(), MainActivity.this,beanManager,hd);
 				voiceAlertDia.creatDialog(false);
 				voiceAlertDia.show();
 				  
 			}break;
 			case MycommandNum.VOICENO:{
 				if(voiceAlertDia!=null){
 					voiceAlertDia.over();
 					voiceAlertDia.dissmiss();
 					voiceAlertDia=null;
 				}
// 				else{
// 					Toast.makeText(MainActivity.this, "对方拒绝通话！", Toast.LENGTH_SHORT).show();
// 				}
 				
 			}break;
 			case MycommandNum.VOICEYES:{
 				if(voiceAlertDia!=null){
 					voiceAlertDia.changeLayout();
 					beanManager.setVioceTransport(true);
 				}
 			}break;
 			case MycommandNum.VOICEING:{
 				if(voiceAlertDia!=null){
 					voiceAlertDia.dissmiss();
 					Toast.makeText(MainActivity.this, "@@对方正在语音中！", Toast.LENGTH_SHORT).show();
 				}
 			}break;
 		
 			}//end of switch
 		}//end of handleMessage
     	
     }

}
