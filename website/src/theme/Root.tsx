import {createTheme, MantineProvider, rem} from "@mantine/core";
import '@mantine/core/styles.css';
import '@mantine/spotlight/styles.css';
import {Spotlight, SpotlightActionData} from "@mantine/spotlight";
import {IconDashboard, IconFileText, IconHome, IconSearch} from "@tabler/icons-react";
const theme = createTheme({
    /** Put your mantine theme override here */
});
const actions: SpotlightActionData[] = [
    {
        id: 'home',
        label: 'Home',
        description: 'Get to home page',
        onClick: () => console.log('Home'),
        leftSection: <IconHome style={{ width: rem(24), height: rem(24) }} stroke={1.5} />,
    },
    {
        id: 'dashboard',
        label: 'Dashboard',
        description: 'Get full information about current system status',
        onClick: () => console.log('Dashboard'),
        leftSection: <IconDashboard style={{ width: rem(24), height: rem(24) }} stroke={1.5} />,
    },
    {
        id: 'documentation',
        label: 'Documentation',
        description: 'Visit documentation to lean more about all features',
        onClick: () => console.log('Documentation'),
        leftSection: <IconFileText style={{ width: rem(24), height: rem(24) }} stroke={1.5} />,
    },
];
const Root = ({children}) => {
    return <>
        <MantineProvider theme={theme}>
            <Spotlight
                actions={actions}
                nothingFound="Nothing found..."
                highlightQuery
                shortcut="mod + J"
                searchProps={{
                    leftSection: <IconSearch style={{ width: rem(20), height: rem(20) }} stroke={1.5} />,
                    placeholder: 'Search...',
                }}
            />
            {children}
        </MantineProvider>
    </>
}

export default Root