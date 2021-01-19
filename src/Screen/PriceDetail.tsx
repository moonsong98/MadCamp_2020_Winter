import React from 'react';
import { Route } from 'react-router-dom';
import NestedGrid from '../components/App/GridLayout';
import Comments from '../components/App/Comments';

function PriceDetail() {
	console.log('Price Detail Called');
	return (
		<div>
			<img alt="이미지 준비중" />
			<Comments />
		</div>
	);
}

export default PriceDetail;
