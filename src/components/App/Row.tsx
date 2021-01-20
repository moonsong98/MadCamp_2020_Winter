import React, { useState } from 'react';
import { makeStyles, createStyles, Theme } from '@material-ui/core/styles';
import { Link } from 'react-router-dom';
// import YouTube from 'react-youtube';
import '../../design/Row.css';
import MediaCard from './Card';

interface RowProps {
	title: string;
	rowMovies: Event[];
	directTo: string;
	// seeDetail:boolean
}

type Movie = { originalName: string; name: string; posterPath: string };

type Event = { title: string; content: string; image: string };

const imgDir = './images';

const useStyles = makeStyles((theme: Theme) =>
	createStyles({
		root: {
			textDecoration: 'none',
		},
	})
);

function Row(props: RowProps) {
	const { title, rowMovies, directTo } = props;
	const [movies, setMovies] = useState<Event[]>(rowMovies);
	const classes = useStyles();

	// useEffect(() => {
	//     // if [], run once when the row loads, and dont run again
	//     async function fetchData() {
	//         // const request = await axios.get(fetchUrl);
	//         setMovies(preparedMovie);
	//     }
	// }, []); // <--- fetchUrl 바뀔때 마다 실행되게 하려면  [] 대신 [fetchUrl] 넣으면 됨

	// setMovies(preparedMovie);

	/*
    useEffect(FUNCTION, []) 형식 
    Row가 load 된 순간 FUNCTION에 해당하는 코드 실행? Row가 load 되면 필요한 정보를 서버에 요청하는 것
    []이렇게 주면 run once when row loads. useEffect 안에서 쓴거는 []안에 적어 줘야 됨. fetchUrl
    await : request를 보내면 답이 올 때까지 기다림
    useEffect(() => {
        async function fetchData() {
            const request = await axios.get(fetchUrl);
            setMoives(request.data.results);
            return request;
        }
        fetchData();
    }, [])
    */
	/*
   
   */

	// 최적화를 위해 나중에 img에 key 속성 줘야 됨
	return (
		<div className="row">
			<h2>{title}</h2>

			<div className="row__posters">
				{/* several row__poster(s) */}
				{movies.map((movie) => (
					<div>
						<div>
							<Link className={classes.root} to={`${directTo}?imgPath=${movie.image}`} textDecoration="none">
								<MediaCard title={movie.title} content={movie.content} imgPath={movie.image} />
							</Link>
						</div>
					</div>
				))}
			</div>
			{/* containers -> posters */}
		</div>
	);
}

export default Row;
