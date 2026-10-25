import { useState } from "react";
import {
  StyleSheet,
  ScrollView,
  StatusBar,
  View,
  Platform,
} from "react-native";
import {
  useSafeAreaInsets,
  SafeAreaProvider,
} from "react-native-safe-area-context";

import * as GoogleAuth from "@react-native-auth/google";
import { GluestackUIProvider } from "../components/ui/gluestack-ui-provider";

import {
  Select,
  SelectTrigger,
  SelectInput,
  SelectIcon,
  SelectPortal,
  SelectBackdrop,
  SelectContent,
  SelectDragIndicator,
  SelectDragIndicatorWrapper,
  SelectItem,
} from "../components/ui/select";
import { Input, InputField } from "../components/ui/input";
import { Button, ButtonText, ButtonSpinner } from "../components/ui/button";
import { ChevronDownIcon } from "../components/ui/icon";
import { Text } from "../components/ui/text";
import { Heading } from "../components/ui/heading";
import "../global.css";

function MainScreen() {
  const [resultText, setResultText] = useState("Waiting Action…");
  const [clientId, setClientId] = useState(
    "[Your_Client_Id_HERE].apps.googleusercontent.com",
  );
  const [scopesText, setScopesText] = useState("openid, email, profile");
  const [action, setAction] = useState("legacySignIn");
  const [loading, setLoading] = useState(false);

  const insets = useSafeAreaInsets();

  const onAuthPress = async () => {
    setLoading(true);
    setResultText("Running Action…");

    try {
      const parsedScopes = scopesText
        .split(",")
        .map((s) => s.trim())
        .filter(Boolean);

      const authFunction = GoogleAuth[action];

      if (!authFunction) {
        throw new Error(`Invalid action: ${action}`);
      }
      let result;

      if (action === "requestScopes") {
        result = await authFunction(parsedScopes);
      } else if (action === "signOut") {
        result = await authFunction();
      } else {
        result = await authFunction({
          clientId,
          scopes: parsedScopes,
          prompt: "consent",
        });
      }

      if (typeof result === "string") {
        setResultText(result);
      } else {
        setResultText(JSON.stringify(result, null, 2));
      }
    } catch (error) {
      setResultText(`Sign-in failed:\n${error?.message || error}`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <GluestackUIProvider mode="light">
      <View style={[styles.container, { paddingTop: insets.top }]}>
        <StatusBar hidden />
        <ScrollView
          contentContainerStyle={[
            styles.scrollViewContent,
            { paddingBottom: insets.bottom + 60 },
          ]}
        >
          <Heading style={styles.heading}>@react-native-auth/google</Heading>
          <Input style={styles.inputSpacing} variant="outline" size="md">
            <InputField
              value={clientId}
              multiline={true}
              onChangeText={setClientId}
              placeholder="Enter Web Client ID"
            />
          </Input>
          <Input style={styles.inputSpacingLarge} variant="outline" size="md">
            <InputField
              value={scopesText}
              onChangeText={setScopesText}
              placeholder="Scopes (comma separated)"
            />
          </Input>
          <View style={styles.selectWrapper}>
            <Select
              selectedValue={action}
              onValueChange={(val) => setAction(val)}
            >
              <SelectTrigger
                variant="outline"
                size="md"
                style={styles.selectTrigger}
              >
                <SelectInput
                  placeholder="Select Action"
                  style={styles.selectInput}
                />
                <SelectIcon as={ChevronDownIcon} style={styles.selectIcon} />
              </SelectTrigger>
              <SelectPortal>
                <SelectBackdrop />
                <SelectContent>
                  <SelectDragIndicatorWrapper>
                    <SelectDragIndicator />
                  </SelectDragIndicatorWrapper>
                  <SelectItem label="SignIn" value="signIn" />
                  <SelectItem label="Legacy SignIn" value="legacySignIn" />
                  <SelectItem label="OneTap SignIn" value="oneTap" />
                  <SelectItem label="Request Scopes" value="requestScopes" />
                  <SelectItem label="Sign Out" value="signOut" />
                </SelectContent>
              </SelectPortal>
            </Select>
          </View>
          <Button
            style={styles.button}
            size="lg"
            onPress={onAuthPress}
            disabled={loading}
          >
            {loading && <ButtonSpinner color="white" style={styles.spinner} />}
            <ButtonText>
              {loading ? "Processing..." : `Run ${action}`}
            </ButtonText>
          </Button>
          <View style={styles.resultContainer}>
            <Text style={styles.resultTitle}>Status / Result:</Text>
            <Text style={styles.resultText}>{resultText}</Text>
          </View>
        </ScrollView>
      </View>
    </GluestackUIProvider>
  );
}

export default function App() {
  return (
    <SafeAreaProvider>
      <MainScreen />
    </SafeAreaProvider>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#FFFFFF",
  },
  scrollViewContent: {
    padding: 24,
    flexGrow: 1,
  },
  heading: {
    textAlign: "center",
    color: "#171717",
    marginBottom: 32,
    marginTop: 16,
    fontSize: 24,
  },
  inputSpacing: {
    marginBottom: 16,
  },
  inputSpacingLarge: {
    marginBottom: 24,
  },
  selectWrapper: {
    marginBottom: 32,
    zIndex: 50,
  },
  selectTrigger: {
    flexDirection: "row",
    justifyContent: "space-between",
  },
  selectInput: {
    flex: 1,
  },
  selectIcon: {
    marginRight: 12,
  },
  button: {
    marginBottom: 32,
    width: "100%",
  },
  spinner: {
    marginRight: 8,
  },
  resultContainer: {
    padding: 16,
    backgroundColor: "#F5F5F5",
    borderRadius: 12,
    borderWidth: 1,
    borderColor: "#E5E5E5",
    minHeight: 150,
  },
  resultTitle: {
    fontWeight: "bold",
    color: "#171717",
    marginBottom: 8,
  },
  resultText: {
    color: "#404040",
    fontSize: 14,
    fontFamily: Platform.OS === "ios" ? "Menlo" : "monospace",
  },
});
