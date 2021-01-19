import React, { useState } from 'react';
import { Link } from 'react-router-dom';
// import YouTube from 'react-youtube';
import '../../design/Row.css';
import MediaCard from './Card';

interface RowProps {
	title: string;
}

function RowInPost(props: RowProps) {
	const { title } = props;

	return (
		<div className="row__post">
			<h1>{title}</h1>
			{/* containers -> posters */}
			<h2>우리는 기가막힌 스타트업 입니다.</h2>
		</div>
	);
}

export default RowInPost;
