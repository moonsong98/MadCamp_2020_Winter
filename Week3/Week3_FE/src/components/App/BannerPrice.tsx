import { Theme, createStyles, makeStyles } from '@material-ui/core';
import React, { useState } from 'react';

interface BannerPrcieProps {
	imgSrc: string;
}

const useStyles = makeStyles((theme: Theme) =>
	createStyles({
		topImg: {
			objectFit: 'cover',
		},
	})
);

function BannerPrice(props: BannerPrcieProps) {
	const classes = useStyles();

	const { imgSrc } = props;
	return (
		<div>
			<img className={classes.topImg} width="100%" height={300} src={imgSrc} alt="이미지 준비중" />
		</div>
	);
}

export default BannerPrice;
