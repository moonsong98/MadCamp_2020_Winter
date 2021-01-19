import React from 'react';
import { createStyles, makeStyles, Theme } from '@material-ui/core/styles';
import TextField from '@material-ui/core/TextField';

interface multLineTxtProps {
	labelTxt: string;
	rowNum: number;
}

const useStyles = makeStyles((theme: Theme) =>
	createStyles({
		root: {
			'& .MuiTextField-root': {
				margin: theme.spacing(1),
				width: '95%',
			},
		},
	})
);

export default function MultilineTextFields(props: multLineTxtProps) {
	const { labelTxt, rowNum } = props;
	const classes = useStyles();
	const [value, setValue] = React.useState('Controlled');

	const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
		setValue(event.target.value);
	};

	return (
		<form className={classes.root} noValidate autoComplete="off">
			<div>
				<TextField id="standard-multiline-static" label={labelTxt} multiline rows={rowNum} />
			</div>
		</form>
	);
}
