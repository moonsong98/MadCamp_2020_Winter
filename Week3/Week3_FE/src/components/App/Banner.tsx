import React, { useState } from 'react';
import { NavLink, Route } from 'react-router-dom';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import SimpleImageSlider from 'react-simple-image-slider';
import '../../design/Banner.css';

const contents = [
	{
		id: 1,
		title: 'aaa',
		bgImageUrl:
			'https://mobiinsidecontent.s3.ap-northeast-2.amazonaws.com/kr/wp-content/uploads/2020/02/10160353/GettyImages-1131776334-scaled.jpg',
	},
	{ id: 2, title: 'bbb', bgImageUrl: 'https://www.lcinternational.it/wp-content/uploads/Start_up.jpg' },
];

const imgs = [
	{
		url:
			'https://mobiinsidecontent.s3.ap-northeast-2.amazonaws.com/kr/wp-content/uploads/2020/02/10160353/GettyImages-1131776334-scaled.jpg',
	},
	{ url: 'https://www.lcinternational.it/wp-content/uploads/Start_up.jpg' },
];

function Banner() {
	return (
		<div>
			<SimpleImageSlider width="100%" height={504} images={imgs} showNavs showBullets />
		</div>
	);
}

export default Banner;
