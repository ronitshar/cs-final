/* eslint-disable no-loop-func */
import React from "react";
import { withStyles, makeStyles } from "@material-ui/core/styles";
import Typography from "@material-ui/core/Typography";
import Card from "@material-ui/core/Card";
import CardActionArea from "@material-ui/core/CardActionArea";
import CardActions from "@material-ui/core/CardActions";
import CardContent from "@material-ui/core/CardContent";
import CardMedia from "@material-ui/core/CardMedia";
import logo from "./logo.svg";
import "./App.css";

const styles = () => ({
  card: {
    minWidth: 600,
    display: "flex",
    flexDirection: "column",
    alignItems: "center"
  },
  media: {
    height: 140
  }
});

class App extends React.Component {
  constructor(props) {
    super(props);
  }
  state = {
    players: [],
    playerData: [],
    playersLoaded: false,
    pointsSorted: [0],
    points: [],
    firstNames: [],
    lastNames: [],
    fgp: [],
    final: [],
    playerDataLoaded: true,
  };
  componentDidMount() {
    fetch("https://cors.io/?http://data.nba.net/10s/prod/v1/2018/players.json")
      .then(res => res.json())
      .then(data => {
        this.setState({ players: data.league.standard, playersLoaded: true });

        for (let i = 0; i < 500; i++) {
          fetch("https://cors.io/?http://data.nba.net/10s/prod/v1/2018/players/" + this.state.players[i].personId + "_profile.json")
            .then(res => res.json())
            .then(newData => {
              if(newData.league.standard.stats.latest.ppg != "-1") {
              this.setState({
                playerData: this.state.playerData.concat({
                  player: newData, info: data.league.standard[i]
                }),
              });
              }

            });
        }
      });

  }

  render() {
    const { classes } = this.props;
    let rankedPoints = (v) => {
      let currentPlayer = v.player.league.standard.stats.latest;
      return currentPlayer.ppg / (1 - Math.pow((0.01 * currentPlayer.fgp, 2)))
    }
    let sortedData = this.state.playerData;
    sortedData.sort((a, b) => {
      return rankedPoints(a) < rankedPoints(b);
    })
    return (
      <div>
      <div className="header">
        NBA players ranked for one season 
      </div>
      <div className="box">
        {
          sortedData.map((val, index) => {
          if(index < 10) {
            return (
              <Card className={classes.card}>
              <CardContent>
                <Typography className={classes.title} color="textSecondary" gutterBottom>
                  {val.info.firstName + " " + val.info.lastName}
                </Typography>
              </CardContent>
            </Card>
            )
          }
        })}
      </div>
      </div>
    );
  }
}

export default withStyles(styles)(App);
