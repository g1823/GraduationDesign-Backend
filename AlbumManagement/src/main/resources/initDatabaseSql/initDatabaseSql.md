用户表(user)：
符合阿里开发者手册表中，表名使用单数形式，主键使用pk_作为前缀，唯一索引使用uk_前缀、普通索引使用idx_前缀
由于密码最终是md5加密，因此固定长度为32位。
建表语句：

CREATE TABLE `user` (
`pk_username` VARCHAR(20) NOT NULL DEFAULT 'null' COMMENT '用户账户，唯一表示',
`password` VARCHAR(32) NOT NULL DEFAULT 'null' COMMENT '用户密码',
`nickname` VARCHAR(50) NOT NULL DEFAULT 'null' COMMENT '用户昵称',
`age` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '年龄',
`sex` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '性别，0代表未知，1为男，2为女',
`avatar` VARCHAR(255) NOT NULL DEFAULT 'null' COMMENT '头像',
`status` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '状态，0为正常，1为禁止登录...',
`introduction` VARCHAR(255) NOT NULL DEFAULT 'null' COMMENT '个人简介',
`create_time` DATETIME NULL DEFAULT NULL COMMENT '注册时间',
`update_time` DATETIME NULL DEFAULT NULL COMMENT '上次登录时间',
PRIMARY KEY (`pk_username`)
)
COMMENT='用户基础信息表'
COLLATE='utf8_general_ci';


图片表(picture):


CREATE TABLE `picture` (
`id` BIGINT NOT NULL AUTO_INCREMENT,
`url` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '图片地址',
`file_size` BIGINT NOT NULL DEFAULT 0 COMMENT '图片大小',
`view_count` INT NOT NULL DEFAULT 0 COMMENT '浏览数量',
`like_count` INT NOT NULL DEFAULT 0 COMMENT '点赞数量',
`comment_count` INT NOT NULL DEFAULT 0 COMMENT '评论数量',
`download_count` INT NOT NULL DEFAULT 0 COMMENT '下载数量',
`category_id` BIGINT NOT NULL DEFAULT 0 COMMENT '所属分类',
`user_id` BIGINT NOT NULL DEFAULT 0 COMMENT '所属用户',
`is_public` TINYINT NOT NULL DEFAULT 0 COMMENT '是否公开',
`picture_name` VARCHAR(255) NOT NULL DEFAULT '0' COMMENT '图片名称',
`description` VARCHAR(255) NOT NULL DEFAULT '0' COMMENT '图片描述',
`create_time` DATETIME NULL DEFAULT NULL COMMENT '创建时间',
PRIMARY KEY (`id`),
CONSTRAINT `FK__user` FOREIGN KEY (`user_id`) REFERENCES `user`  (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION
CONSTRAINT `FK_picture_category` FOREIGN KEY (`category_id`) REFERENCES  `category` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION;
)
COLLATE='utf8_general_ci';

分类表(category):


CREATE TABLE `category` (
`id` BIGINT NOT NULL AUTO_INCREMENT,
`user_id` BIGINT NOT NULL DEFAULT 0 COMMENT '创建用户',
`category_name` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '分类名称',
`type` TINYINT NOT NULL DEFAULT 0 COMMENT '表示是否为系统分类',
`parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父分类不为0表示其为子分类',
`description` VARCHAR(255) NOT NULL DEFAULT '0' COMMENT '分类描述',
`create_time` DATETIME NULL DEFAULT NULL COMMENT '创建时间',
PRIMARY KEY (`id`)
)
COLLATE='utf8_general_ci';

初始数据：

INSERT INTO category VALUES
(0,0,"未分类",0,"暂未分类",NOW()),
(1,0,"动物",0,"动物类别",NOW()),
(2,0,"食物",0,"食物类别",NOW()),
(3,0,"植物",0,"植物类别",NOW()),
(4,0,"风景",0,"风景类别",NOW()),
(5,0,"交通工具",0,"交通工具类别",NOW()),
(6,0,"体育用具",0,"体育用具类别",NOW()),
(7,0,"音乐器材",0,"音乐器材类别",NOW()),
(8,0,"家具",0,"家具类别",NOW()),
(9,0,"建筑",0,"建筑类别",NOW()),
(10,0,"电子产品",0,"电子产品类别",NOW()),
(11,0,"衣服装饰品",0,"衣服装饰品类别",NOW()),
(12,0,"人",0,"人",NOW())

标签表(tag):

CREATE TABLE `tag` (
`id` BIGINT NOT NULL AUTO_INCREMENT,
`tag_name` VARCHAR(50) NOT NULL DEFAULT 'null' COMMENT '标签名称',
`use_count` INT NOT NULL DEFAULT 0 COMMENT '使用次数',
`user_id` BIGINT NOT NULL DEFAULT 0 COMMENT '创建者，0代表系统标签',
`create_time` DATETIME NULL DEFAULT NULL COMMENT '创建时间',
PRIMARY KEY (`id`)
)
COMMENT='图片标签表'
COLLATE='utf8_general_ci';


标签_图片对应表(pic_tag)：
因为标签和图片是多对多的对应关系，因此需要第三个表记录二者关系

CREATE TABLE `pic_tag` (
`id` BIGINT NOT NULL AUTO_INCREMENT,
`pic_id` BIGINT NOT NULL DEFAULT '0',
`tag_id` BIGINT NOT NULL DEFAULT '0',
PRIMARY KEY (`id`),
CONSTRAINT `FK__picture` FOREIGN KEY (`pic_id`) REFERENCES  `picture` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION,
CONSTRAINT `FK__tag` FOREIGN KEY (`tag_id`) REFERENCES `tag`  (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION
)
COMMENT='图片标签对应表'
COLLATE='utf8_general_ci';



INSERT INTO tag VALUES
(0,"ak47",0,0,now())
(1,"美国国旗",0,0,now())
(2,"背包",0,0,now())
(3,"棒球棍",0,0,now())
(4,"棒球手套",0,0,now())
(5,"篮球框",0,0,now()),
(6,"蝙蝠",0,0,now())
(7,"浴缸",0,0,now())
(8,"熊",0,0,now())
(9,"啤酒杯",0,0,now())
(10,"台球",0,0,now()),
(11,"双筒望远镜",0,0,now())
(12,"观鸟池",0,0,now())
(13,"飞艇",0,0,now())
(14,"盆景",0,0,now())
(15,"吊杆箱",0,0,now()),
(16,"保龄球",0,0,now())
(17,"保龄球棍",0,0,now())
(18,"拳击手套",0,0,now())
(19,"大脑",0,0,now())
(20,"面包机",0,0,now()),
(21,"佛像",0,0,now())
(22,"推土机",0,0,now())
(23,"蝴蝶",0,0,now())
(24,"仙人掌",0,0,now())
(25,"蛋糕",0,0,now()),
(26,"计算器",0,0,now())
(27,"骆驼",0,0,now())
(28,"大炮",0,0,now())
(29,"独木舟",0,0,now())
(30,"汽车轮胎",0,0,now()),
(31,"动漫人物-cartman",0,0,now())
(32,"光盘",0,0,now())
(33,"蜈蚣",0,0,now())
(34,"麦片盒",0,0,now())
(35,"吊灯",0,0,now()),
(36,"棋盘",0,0,now())
(37,"黑猩猩",0,0,now())
(38,"筷子",0,0,now())
(39,"蟑螂",0,0,now())
(40,"咖啡杯",0,0,now()),
(41,"棺材",0,0,now())
(42,"硬币",0,0,now())
(43,"彗星",0,0,now())
(44,"电脑键盘",0,0,now())
(45,"电脑显示器",0,0,now()),
(46,"电脑鼠标",0,0,now())
(47,"海螺",0,0,now())
(48,"鸬鹚",0,0,now())
(49,"有盖马车",0,0,now())
(50,"牛仔帽",0,0,now()),
(51,"螃蟹",0,0,now())
(52,"桌面地球仪",0,0,now())
(53,"钻戒",0,0,now())
(54,"骰子",0,0,now())
(55,"狗",0,0,now()),
(56,"海豚",0,0,now())
(57,"门把手",0,0,now())
(58,"吸管",0,0,now())
(59,"鸭子",0,0,now())
(60,"哑铃",0,0,now()),
(61,"埃菲尔铁塔",0,0,now())
(62,"电吉他",0,0,now())
(63,"大象",0,0,now())
(64,"麋鹿",0,0,now())
(65,"母羊",0,0,now()),
(66,"眼镜",0,0,now())
(67,"蕨类植物",0,0,now())
(68,"战斗机",0,0,now())
(69,"灭火器",0,0,now())
(70,"消防栓",0,0,now()),
(71,"消防车",0,0,now())
(72,"烟花",0,0,now())
(73,"手电筒",0,0,now())
(74,"软盘",0,0,now())
(75,"足球头盔",0,0,now()),
(76,"法国角(乐器)",0,0,now())
(77,"煎蛋",0,0,now())
(78,"飞盘",0,0,now())
(79,"青蛙",0,0,now())
(80,"煎锅",0,0,now())
(81,"银河系",0,0,now()),
(82,"气泵",0,0,now())
(83,"长颈鹿",0,0,now())
(84,"山羊",0,0,now())
(85,"金门大桥",0,0,now()),
(86,"金鱼",0,0,now())
(87,"高尔夫球",0,0,now())
(88,"鹅",0,0,now())
(89,"大猩猩",0,0,now())
(90,"大熊猫",0,0,now()),
(91,"葡萄",0,0,now())
(92,"蚱蜢",0,0,now())
(93,"吉他拨片",0,0,now())
(94,"汉堡",0,0,now())
(95,"吊床",0,0,now()),
(96,"口琴",0,0,now())
(97,"竖琴",0,0,now())
(98,"大键琴",0,0,now())
(99,"玳瑁",0,0,now())
(100,"耳机",0,0,now()),
(101,"直升机",0,0,now())
(102,"芙蓉",0,0,now())
(103,"侯默·辛普森",0,0,now())
(104,"马",0,0,now())
(105,"鲎",0,0,now()),
(106,"热气球",0,0,now())
(107,"热狗",0,0,now())
(108,"热水浴缸",0,0,now())
(109,"沙漏",0,0,now())
(110,"家蝇",0,0,now()),
(111,"人类骨骼",0,0,now())
(112,"蜂鸟",0,0,now())
(113,"鹮(一种鸟)",0,0,now())
(114,"冰淇淋蛋卷",0,0,now())
(115,"鬣蜥",0,0,now()),
(116,"mp3",0,0,now())
(117,"虹膜(一种花)",0,0,now())
(118,"耶稣基督",0,0,now())
(119,"操纵杆",0,0,now())
(120,"袋鼠",0,0,now()),
(121,"皮划艇",0,0,now())
(122,"帆船",0,0,now())
(123,"虎鲸",0,0,now())
(124,"刀",0,0,now())
(125,"梯子",0,0,now()),
(126,"笔记本电脑",0,0,now())
(127,"车床",0,0,now())
(128,"豹子",0,0,now())
(129,"车牌",0,0,now())
(130,"灯泡",0,0,now()),
(131,"灯塔",0,0,now())
(132,"闪电",0,0,now())
(133,"骆驼",0,0,now())
(134,"邮箱",0,0,now())
(135,"曼陀林(乐器)",0,0,now()),
(136,"火星",0,0,now())
(137,"床垫",0,0,now())
(138,"扩音器",0,0,now())
(139,"烛台",0,0,now())
(140,"显微镜",0,0,now()),
(141,"微波炉",0,0,now())
(142,"尖塔",0,0,now())
(143,"牛头怪",0,0,now())
(144,"摩托车",0,0,now())
(145,"山地自行车",0,0,now()),
(146,"蘑菇",0,0,now())
(147,"贻贝",0,0,now())
(148,"领带",0,0,now())
(149,"章鱼",0,0,now())
(150,"鸵鸟",0,0,now()),
(151,"猫头鹰",0,0,now())
(152,"掌机",0,0,now())
(153,"棕榈树",0,0,now())
(154,"回形针",0,0,now())
(155,"碎纸机",0,0,now()),
(156,"pci卡(电路板)",0,0,now())
(157,"企鹅",0,0,now())
(158,"人",0,0,now())
(159,"pez分配器",0,0,now())
(160,"复印机",0,0,now()),
(161,"野餐桌",0,0,now())
(162,"扑克牌",0,0,now())
(163,"豪猪",0,0,now())
(164,"婴儿车",0,0,now())
(165,"螳螂",0,0,now()),
(166,"金字塔",0,0,now())
(167,"浣熊",0,0,now())
(168,"射电望远镜",0,0,now())
(169,"彩虹",0,0,now())
(170,"冰箱",0,0,now()),
(171,"左轮手枪",0,0,now())
(172,"步枪",0,0,now())
(173,"旋转电话",0,0,now())
(174,"轮盘",0,0,now())
(175,"马鞍",0,0,now()),
(176,"土星",0,0,now())
(177,"校车",0,0,now())
(178,"蝎子",0,0,now())
(179,"螺丝刀",0,0,now())
(180,"平衡车",0,0,now()),
(181,"自动割草机",0,0,now())
(182,"六分仪",0,0,now())
(183,"乐谱",0,0,now())
(184,"滑板",0,0,now())
(185,"臭鼬",0,0,now()),
(186,"摩天大楼",0,0,now())
(187,"烟囱",0,0,now())
(188,"蜗牛",0,0,now())
(189,"蛇",0,0,now())
(190,"运动鞋",0,0,now()),
(191,"雪地摩托",0,0,now())
(192,"足球",0,0,now())
(193,"袜子",0,0,now())
(194,"汽水罐",0,0,now())
(195,"意大利面",0,0,now()),
(196,"快艇",0,0,now())
(197,"蜘蛛",0,0,now())
(198,"勺子",0,0,now())
(199,"彩色玻璃",0,0,now())
(200,"海星",0,0,now()),
(201,"方向盘",0,0,now())
(202,"马镫",0,0,now())
(203,"向日葵",0,0,now())
(204,"超人",0,0,now())
(205,"寿司",0,0,now()),
(206,"天鹅",0,0,now())
(207,"瑞士军刀",0,0,now())
(208,"剑",0,0,now())
(209,"注射器",0,0,now())
(210,"铃鼓",0,0,now()),
(211,"茶壶",0,0,now())
(212,"泰迪熊",0,0,now())
(213,"圆锥形帐篷",0,0,now())
(214,"电话亭",0,0,now())
(215,"网球",0,0,now()),
(216,"网球场",0,0,now())
(217,"网球拍",0,0,now())
(218,"经纬仪",0,0,now())
(219,"烤面包机",0,0,now())
(220,"番茄",0,0,now()),
(221,"墓碑",0,0,now())
(222,"礼帽",0,0,now())
(223,"旅游自行车",0,0,now())
(224,"比萨斜塔",0,0,now())
(225,"红绿灯",0,0,now()),
(226,"跑步机",0,0,now())
(227,"三角龙",0,0,now())
(228,"三轮车",0,0,now())
(229,"三叶虫",0,0,now())
(230,"三脚架",0,0,now()),
(231,"t恤",0,0,now())
(232,"音叉",0,0,now())
(233,"镊子",0,0,now())
(234,"雨伞",0,0,now())
(235,"独角兽",0,0,now()),
(236,"录像机",0,0,now())
(237,"视频投影仪",0,0,now())
(238,"洗衣机",0,0,now())
(239,"手表",0,0,now())
(240,"瀑布",0,0,now()),
(241,"西瓜",0,0,now())
(242,"焊接面罩",0,0,now())
(243,"独轮车",0,0,now())
(244,"风车",0,0,now())
(245,"酒瓶",0,0,now()),
(246,"木琴",0,0,now())
(247,"圆顶小帽",0,0,now())
(248,"溜溜球",0,0,now())
(249,"斑马",0,0,now())
(250,"飞机",0,0,now()),
(251,"汽车",0,0,now())
(252,"人脸",0,0,now())
(253,"灰狗",0,0,now())
(254,"网球鞋",0,0,now())
(255,"蟾蜍",0,0,now())
评论表(comment)：

CREATE TABLE `comment` (
`id` BIGINT NOT NULL AUTO_INCREMENT,
`content` VARCHAR(1024) NOT NULL DEFAULT '' COMMENT '评论内容',
`user_id` BIGINT NOT NULL DEFAULT 0 COMMENT '评论用户的id',
`pic_id` BIGINT NOT NULL DEFAULT 0 COMMENT '被评论照片的id',
`father_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父级评论，0代表自身为一级',
`create_date` DATETIME NOT NULL,
PRIMARY KEY (`id`)
)
COMMENT='评论表'
COLLATE='utf8_general_ci';

点赞表(like)：

CREATE TABLE `pic_like` (
`id` BIGINT NOT NULL AUTO_INCREMENT,
`user_id` BIGINT NOT NULL DEFAULT 0 COMMENT '点赞用户id',
`pic_id` BIGINT NOT NULL DEFAULT 0 COMMENT '被点赞图片',
`create_time` DATETIME NULL DEFAULT NULL COMMENT '创建时间',
PRIMARY KEY (`id`)
)
COMMENT='点赞表' COLLATE='utf8_general_ci';

好友表(Friend):

CREATE TABLE `friend` (
`id` BIGINT NOT NULL AUTO_INCREMENT,
`apply_id` BIGINT NOT NULL DEFAULT '0' COMMENT '申请者id',
`receive_id` BIGINT NOT NULL DEFAULT '0' COMMENT '接收者id',
`status` TINYINT NOT NULL DEFAULT '0' COMMENT '状态',
PRIMARY KEY (`id`)
)
COMMENT='好友表' COLLATE='utf8_general_ci';















































