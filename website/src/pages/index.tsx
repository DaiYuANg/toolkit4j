import clsx from "clsx";
import Link from "@docusaurus/Link";
import useDocusaurusContext from "@docusaurus/useDocusaurusContext";
import Layout from "@theme/Layout";
import HomepageFeatures from "@site/src/components/HomepageFeatures";
import Heading from "@theme/Heading";
import "@fontsource/jetbrains-mono"; // Defaults to weight 400
import "@fontsource/jetbrains-mono/400.css"; // Specify weight
import "@fontsource/jetbrains-mono/400-italic.css"; // Specify weight and style

import styles from "./index.module.css";
import { Container, Stack, Text, Title } from "@mantine/core";

const rootLinkCss = [
  "button",
  "button--secondary",
  "button--lg",
  "transform h-64",
  "bg-blue-400",
  "w-80 ",
  "transition duration-500",
  "hover:scale-125",
  "hover:bg-white-600",
  "flex",
  "justify-center",
  "items-center",
].join(" ");

function HomepageHeader() {
  const { siteConfig } = useDocusaurusContext();
  return (
    <header className={clsx("hero hero--primary", styles.heroBanner)}>
      <Container className="container">
        <Heading as={"h1"} className="hero__title">
          {siteConfig.title}
        </Heading>
        <Text
          size="lg"
          fw={900}
          variant="gradient"
          gradient={{ from: "blue", to: "cyan", deg: 90 }}
          className="hero__subtitle"
        >
          {siteConfig.tagline}
        </Text>
        <Stack className={styles.buttons}>
          <Link className={rootLinkCss} to="/docs/intro">
            Docusaurus Tutorial - 5min ⏱️
          </Link>
        </Stack>
      </Container>
    </header>
  );
}

export default function Home() {
  const { siteConfig } = useDocusaurusContext();
  return (
    <Layout
      title={`Hello from ${siteConfig.title}`}
      description="Description will go into a meta tag in <head />"
    >
      <HomepageHeader />
      <main>
        <HomepageFeatures />
      </main>
    </Layout>
  );
}
