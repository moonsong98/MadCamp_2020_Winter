import React from 'react';
import { Route, useLocation } from 'react-router-dom';
import queryString from 'query-string';
import BannerPrice from '../components/App/BannerPrice';
import NestedGrid from '../components/App/GridLayout';

function PostPrices() {
	const location = useLocation();
	const { search } = location;
	const queryObj = queryString.parse(search);
	const { imgPath } = queryObj;
	console.log(location);
	console.log(imgPath);
	let imgPathStr = '';
	if (imgPath != null) {
		imgPathStr = imgPath.toString();
	}
	return (
		<div>
			<BannerPrice imgSrc={imgPathStr} />
			<NestedGrid />
		</div>
	);
}

export default PostPrices;
