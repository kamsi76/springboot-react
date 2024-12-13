package kr.co.infob.config.security.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import kr.co.infob.common.database.entity.MenuInfo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserMenuVo extends MenuInfo{

	private UserMenuVo parent;

	private List<UserMenuVo> subMenus;

	private List<MenuRoleVo> menuRoles;

	public void setSubMenus(List<UserMenuVo> subMenus) {

		for( UserMenuVo subMenu: subMenus ) {
			subMenu.setParent(this);
		}

		this.subMenus = subMenus;
	}

	public void addSubMenu(UserMenuVo subMenu) {
		if( this.subMenus == null ) {
			this.subMenus = new ArrayList<UserMenuVo>();
		}

		subMenus.add(subMenu);
		subMenu.setParent(this);
	}

	/**
	 * 접속 URL
	 * @return
	 */
	public String getRealUrl(){
		if(CollectionUtils.isEmpty(subMenus)){
			return getMenuUrl();
		}

		return subMenus.get(0).getRealUrl();
	}

	@JsonIgnore
	public UserMenuVo[] getLocation(){
		UserMenuVo[] result = new UserMenuVo[getMenuLevel()];
		setLocation(result, this);
		return result;
	}

	@JsonIgnore
	private void setLocation(UserMenuVo[] result, UserMenuVo menu){
		result[menu.getMenuLevel()-1] = menu;
		if(menu.getParent() != null && menu.getParent().getMenuSn() != null ){
			setLocation(result, menu.getParent());
		}
	}
}
