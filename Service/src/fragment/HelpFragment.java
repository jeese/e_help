package fragment;

import java.util.ArrayList;
import java.util.List;

import radialdemo.RadialMenuItem;
import radialdemo.RadialMenuWidget;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import client.ui.R;
import client.ui.SendHealthHelpActivity;
import client.ui.SendLifeHelpActivity;
import client.ui.SendSecurityHelpActivity;

public class HelpFragment extends Fragment{
	private RadialMenuWidget pieMenu;
	public RadialMenuItem menuItem, menuClose1Item, menuClose2Item;
	public RadialMenuItem firstChildItem, secondChildItem, thirdChildItem;
	private List<RadialMenuItem> children = new ArrayList<RadialMenuItem>();
	private View view;

	@SuppressWarnings("serial")
	public HelpFragment(){
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater,
    ViewGroup container, Bundle savedInstanceState) {       

		
		
		 //pieMenu.addMenuEntry(menuItem);
		ViewGroup p = (ViewGroup) view.getParent(); 
        if (p != null) { 
            p.removeAllViewsInLayout(); 
        } 

        return view;
    }

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view=View.inflate(getActivity(),R.layout.help,null); 
		pieMenu =(RadialMenuWidget)view.findViewById(R.id.raidal);

		menuClose2Item = new RadialMenuItem(getString(R.string.help2), getString(R.string.help2));
		
		
		menuItem = new RadialMenuItem(getString(R.string.help1),
				getString(R.string.help1));
		
		firstChildItem = new RadialMenuItem(getString(R.string.life),
				getString(R.string.life));
		firstChildItem.setDisplayIcon(R.drawable.ic_action_life);
		firstChildItem
				.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
					@Override
					public void execute() {
						// Can edit based on preference. Also can add animations
						// here.
						getActivity().startActivity(new Intent(getActivity(),SendLifeHelpActivity.class));
						pieMenu.dismiss();
						Toast.makeText(getActivity(), R.string.life, Toast.LENGTH_SHORT).show();
					}
				});

		secondChildItem = new RadialMenuItem(getString(R.string.security),
				getString(R.string.security));
		secondChildItem.setDisplayIcon(R.drawable.ic_action_security);
		secondChildItem
				.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
					@Override
					public void execute() {
						// Can edit based on preference. Also can add animations
						// here.
						getActivity().startActivity(new Intent(getActivity(),SendSecurityHelpActivity.class));
						pieMenu.dismiss();
						Toast.makeText(getActivity(), R.string.security, Toast.LENGTH_SHORT).show();
					}
				});

		thirdChildItem = new RadialMenuItem(getString(R.string.health),
				getString(R.string.health));
		thirdChildItem.setDisplayIcon(R.drawable.ic_action_health);
		thirdChildItem
				.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
					@Override
					public void execute() {
						// Can edit based on preference. Also can add animations
						// here.
						getActivity().startActivity(new Intent(getActivity(),SendHealthHelpActivity.class));
						pieMenu.dismiss();
						Toast.makeText(getActivity(), R.string.health, Toast.LENGTH_SHORT).show();
					}
				});


		//if(children.size() != 3)
		//{
		children.add(firstChildItem);
		children.add(secondChildItem);
		children.add(thirdChildItem);
		//}
		menuItem.setMenuChildren(children);
		menuClose2Item.setMenuChildren(children);

		// pieMenu.setDismissOnOutsideClick(true, menuLayout);
		pieMenu.setAnimationSpeed(100);
		
		DisplayMetrics dm = getResources().getDisplayMetrics();
		int h = dm.heightPixels;
		int w = dm.widthPixels;
		pieMenu.setCenterLocation(w/2, h/2-100);
		pieMenu.setIconSize(w/30, w/30);
		pieMenu.setTextSize(w/40);
		pieMenu.setInnerRingRadius(0, w/10);
		pieMenu.setOuterRingRadius(w/10+5, w/5);
		
		pieMenu.setOutlineColor(Color.BLACK, 5);
		//pieMenu.setInnerRingColor(0xAA66CC, 180);   //purple
		//pieMenu.setInnerRingColor(0xEECA5B, 180);   //yellow
		pieMenu.setInnerRingColor(0xF4A460, 180);
		//pieMenu.setOuterRingColor(0x0099CC, 180);   //blue
		//pieMenu.setOuterRingColor(0x2691e, 180);    //green
		pieMenu.setOuterRingColor(0xFF4500, 180);
		pieMenu.setOuterRingColor(0x6495ED, 0x3CB371, 0xF08080);
		
		//pieMenu.setHeader("Test Menu", 20);
		//pieMenu.setCenterCircle(menuClose1Item);

		pieMenu.addMenuEntry(new ArrayList<RadialMenuItem>() {
			{
				add(menuItem);
				add(menuClose2Item);
				//add(menuClose1Item);
			}
		});
	}
	
}
