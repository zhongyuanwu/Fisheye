package com.iyuile.caelum.fragment;

/**
 * 
 * @Description 懒加载
 * @ClassName {@link BaseLazyFragment}
 * @author WangYao
 * @version 1
 * @Date 2016-4-3 下午6:25:15
 */
public abstract class BaseLazyFragment extends BaseFragment {
	protected boolean isVisible;

	/**
	 * 在这里实现Fragment数据的缓加载.
	 * 
	 * @param isVisibleToUser
	 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (getUserVisibleHint()) {
			isVisible = true;
			onVisible();
		} else {
			isVisible = false;
			onInvisible();
		}
	}

	/**
	 * 可见
	 */
	protected void onVisible() {
		lazyLoad();
	}

	/**
	 * 隐藏不可见
	 */
	protected void onInvisible() {
	}

	/**
	 * 加载数据
	 */
	protected abstract void lazyLoad();

}
