# EMusic——基于Android的简易音乐播放器

Ⅰ、功能简介：
	本音乐播放器实现的基本功能有播放、暂停、上/下一首、歌曲列表、播放模式、扫描SD卡音频文件、摇一摇切歌等。
  
Ⅱ、开发工具：
	Android Studio 2.2

Ⅲ、详细设计：

	※ Class MainActivity   主界面各种功能的实现，包括播放控制、线程、摇一摇监听等

	※ Class ListActivity      播放列表的实现，设置列表点击事件

	※ Class MusicAdapter   音乐信息适配器，将音乐加入音乐列表

	※ Class MusicData        存储音乐各项信息

	※ Class ReaderMusic     从SD卡搜寻音频文件

	※Class WelcomeActivity  欢迎页面

	※Class MenuActivity     关于页面        


