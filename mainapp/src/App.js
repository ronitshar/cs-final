import React from 'react';
import { withStyles, makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import Card from '@material-ui/core/Card';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import CardMedia from '@material-ui/core/CardMedia';
import logo from './logo.svg';
import './App.css';

const styles = () => ({
  card: {
    minWidth: 600,
    display: "flex",
    flexDirection: "column",
    alignItems: "center"
  },
  media: {
    height: 140,
  },
});


class App extends React.Component {
  constructor(props) {
    super(props);
  }
  state = {
    players: [

    ],
    playerData: [

    ]
  }
  componentDidMount() {
    fetch('https://cors.io/?http://data.nba.net/10s/prod/v1/2018/players.json').then(res => res.json()).then(data => {
      const playerIds = [];
      for(let i = 0; i < data.league.standard.length; i++) {
        playerIds.push(data.league.standard[i].personId);
      }
      this.setState({ players: playerIds })
    });
    const playerData = [];
    for(let i = 0; i < 500; i++) {
      fetch('http://data.nba.net/10s/prod/v1/2018/players/' + this.state.players[i] + '_profile.json').then(res => res.json()).then(data => {
        playerData.push(data.league.standard);
      })
    }
    this.setState({ playerData });
  }
  render() {
    const { classes } = this.props;
    return (
      <div className='box'>
        {this.state.players.map(val => {
          return (
              <Card className={classes.card}>
                <CardContent>
                  <Typography gutterBottom variant="h5" component="h2">
                    {val}
                  </Typography>
                  {/* <Typography variant="body2" color="textSecondary" component="p">
                    Lizards are a widespread group of squamate reptiles, with over 6,000 species, ranging
                    across all continents except Antarctica
                  </Typography> */}
                </CardContent>
            </Card>
          );
        })}
      </div>
    );
  }
}

export default withStyles(styles)(App);
