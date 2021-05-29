export const formatDate = (d) => {
  let [month, date, year] = new Date(d + "Z")
    .toLocaleDateString("en-US")
    .split("/");
  return `${date}.${month}.${year}`;
};