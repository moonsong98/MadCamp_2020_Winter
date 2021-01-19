import React, { useState } from 'react';

interface BannerPrcieProps {
	imgSrc: string;
}

function BannerPrice(props: BannerPrcieProps) {
	const { imgSrc } = props;
	return (
		<div>
			<img width="100%" height={300} src={imgSrc} alt="이미지 준비중" />
		</div>
	);
}

export default BannerPrice;
